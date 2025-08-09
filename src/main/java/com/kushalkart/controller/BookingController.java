package com.kushalkart.controller;

import com.kushalkart.dto.BookingRequest;
import com.kushalkart.dto.BookingUpdateRequest;
import com.kushalkart.dto.BookingResponse;
import com.kushalkart.entity.Booking;
import com.kushalkart.entity.MasterTransactionHistory;
import com.kushalkart.model.CustomUserDetails;
import com.kushalkart.service.BookingService;
import com.kushalkart.service.CashgramService;
import com.kushalkart.repository.BookingRepository;
import com.kushalkart.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private BookingService bookingService;
    @Autowired
    private CashgramService cashgramService;
    @Autowired
    private BookingRepository bookingRepo;
    @Autowired
    private TransactionRepository txRepo;

    // Example static IDs for your DB setup (customize as needed!)
    private static final Long PAYMENT_TYPE_CASHGRAM_ID = 1L;
    private static final Long TRANSACTION_STATUS_PENDING_ID = 1L;
    private static final Long TRANSACTION_STATUS_PAID_ID = 2L;
    private static final Long TRANSACTION_STATUS_FAILED_ID = 3L;
    private static final Long BALANCE_TYPE_REAL_MONEY_ID = 1L;          // From your balance_type table
    private static final Long TRANSACTION_TYPE_BOOKING_PAYOUT_ID = 1L; // Set to real value per your app

    @PostMapping
    public ResponseEntity<?> bookWorker(@RequestBody BookingRequest request) {
        log.info("DEBUG /api/bookings called with request: {}", request);
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getUserId();

            Booking booking = bookingService.createBooking(userId, request);

            log.info("DEBUG Booking created: {}", booking);

            onBookingSuccess(booking, userId); // Pass userId for transaction history

            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            log.error("ERROR in /api/bookings: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Something went wrong: " + e.getMessage(), "error")
            );
        }
    }

    @PutMapping("/{bookingId}")
    public ResponseEntity<?> modifyBooking(
            @PathVariable Long bookingId,
            @RequestBody BookingUpdateRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getUserId();

            Booking updatedBooking = bookingService.updateBooking(userId, bookingId, request);
            return ResponseEntity.ok(updatedBooking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Failed to update booking: " + e.getMessage(), "error")
            );
        }
    }

    @GetMapping("/mine")
    public ResponseEntity<?> getMyBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getUserId();

            Page<BookingResponse> bookings = bookingService.getBookingsForConsumer(userId, status, page, size);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Something went wrong: " + e.getMessage(), "error")
            );
        }
    }

    // Error response DTO
    public static class ErrorResponse {
        private String message;
        private String status;

        public ErrorResponse(String message, String status) {
            this.message = message;
            this.status = status;
        }
        public String getMessage() { return message; }
        public String getStatus() { return status; }
    }

    /**
     * Triggers a Cashgram link for the booking and stores the transaction history.
     */
    public void onBookingSuccess(Booking booking, Long userId) {
        String cashgramId = "booking-" + booking.getId();
        String link = cashgramService.createCashgram(
                cashgramId,
                booking.getAmount(),
                booking.getConsumerName(),
                booking.getConsumerPhone(),
                booking.getConsumerEmail(),
                LocalDate.now().plusDays(7).toString(),
                "Booking payout"
        );

        // Store transaction
        MasterTransactionHistory tx = new MasterTransactionHistory();
        tx.setBookingId(booking.getId());
        tx.setUserId(userId);     // <- Set userId from authenticated user!
        tx.setAmount(booking.getAmount());
        tx.setPaymentTypeId(PAYMENT_TYPE_CASHGRAM_ID);
        tx.setTransactionStatusId(TRANSACTION_STATUS_PENDING_ID);
        tx.setBalanceTypeId(BALANCE_TYPE_REAL_MONEY_ID);                 // set to 1 for real money
        tx.setTransactionTypeId(TRANSACTION_TYPE_BOOKING_PAYOUT_ID);     // set to correct ID (e.g., 1)
        tx.setRemarks("Cashgram created. Link: " + link);
        txRepo.save(tx);

        booking.setPaymentStatus(Booking.PaymentStatus.PENDING);
        bookingRepo.save(booking);

        // TODO: Send link via email or SMS to consumer
    }

    /**
     * Handles webhook callback from Cashfree.
     */
    public void handleCashgramWebhook(String cashgramId, String status) {
        long bookingId = extractBookingId(cashgramId);
        Optional<Booking> optBooking = bookingRepo.findById(bookingId);

        if (!optBooking.isPresent()) {
            // Optionally log invalid cashgram notifications
            return;
        }
        Booking booking = optBooking.get();

        if ("REDEEMED".equalsIgnoreCase(status)) {
            booking.setPaymentStatus(Booking.PaymentStatus.PAID);
            updateTransactionStatus(bookingId, TRANSACTION_STATUS_PAID_ID);
        } else {
            booking.setPaymentStatus(Booking.PaymentStatus.FAILED);
            updateTransactionStatus(bookingId, TRANSACTION_STATUS_FAILED_ID);
        }

        bookingRepo.save(booking);
    }

    /** Utility to extract bookingId from Cashgram string. */
    private long extractBookingId(String cashgramId) {
        return Long.parseLong(cashgramId.replace("booking-", ""));
    }

    /**
     * Update transaction status in master_transaction_history for this booking.
     */
    private void updateTransactionStatus(long bookingId, Long statusId) {
        MasterTransactionHistory tx = txRepo.findByBookingId(bookingId);
        if(tx != null) {
            tx.setTransactionStatusId(statusId);
            txRepo.save(tx);
        }
    }
}
