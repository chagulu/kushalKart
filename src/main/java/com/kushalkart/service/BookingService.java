package com.kushalkart.service;

import com.kushalkart.dto.BookingRequest;
import com.kushalkart.dto.BookingResponse;
import com.kushalkart.dto.BookingUpdateRequest;
import com.kushalkart.entity.Booking;
import com.kushalkart.entity.User;
import com.kushalkart.admin.entity.Worker;
import com.kushalkart.repository.BookingRepository;
import com.kushalkart.repository.UserRepository;
import com.kushalkart.admin.repository.WorkerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BookingService {

    private final WorkerRepository workerRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public BookingService(WorkerRepository workerRepository,
                          UserRepository userRepository,
                          BookingRepository bookingRepository) {
        this.workerRepository = workerRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    /**
     * Create a new booking:
     * - Sets amount using worker's rate_per_hour
     * - Sets consumer name, phone, email from User
     */
    public Booking createBooking(Long consumerId, BookingRequest dto) {
        Booking booking = new Booking();
        booking.setConsumerId(consumerId);
        booking.setWorkerId(dto.getWorkerId());
        booking.setScheduledTime(dto.getScheduledTime());
        booking.setStatus(Booking.Status.PENDING);
        booking.setPaymentStatus(Booking.PaymentStatus.PENDING);
        booking.setServiceId(dto.getServiceId());

        // Fetch User for consumer info
        User user = userRepository.findById(consumerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Set address from User if present
        if (user.getAddress() != null) {
            booking.setAddress(user.getAddress().getFullAddress());
        } else {
            booking.setAddress(null);
        }

        // Set consumer name, phone, email based on available fields
        if (user.getName() != null) { // fixed from getFullName()
            booking.setConsumerName(user.getName());
        }
        if (user.getMobile() != null) { // fixed from getPhone()
            booking.setConsumerPhone(user.getMobile());
        }
        if (user.getEmail() != null) {
            booking.setConsumerEmail(user.getEmail());
        }

        // Fetch Worker for rate_per_hour
        Worker worker = workerRepository.findById(dto.getWorkerId())
                .orElseThrow(() -> new RuntimeException("Worker not found"));
        BigDecimal ratePerHour = worker.getRatePerHour();
        booking.setAmount(ratePerHour); // Optionally multiply by duration if needed

        // Optionally set worker's service name
        booking.setService(worker.getService());

        return bookingRepository.save(booking);
    }

    public Booking updateBooking(Long consumerId, Long bookingId, BookingUpdateRequest request) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getConsumerId().equals(consumerId)) {
            throw new RuntimeException("Unauthorized: You do not own this booking");
        }

        switch (request.getAction().toLowerCase()) {
            case "reschedule":
                if (request.getScheduledTime() == null) {
                    throw new RuntimeException("Scheduled time required for reschedule");
                }
                booking.setScheduledTime(request.getScheduledTime());
                break;

            case "cancel":
                booking.setStatus(Booking.Status.CANCELLED);
                break;

            default:
                throw new RuntimeException("Invalid action. Use 'reschedule' or 'cancel'");
        }

        return bookingRepository.save(booking);
    }

    public Page<BookingResponse> getBookingsForConsumer(Long consumerId, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (status != null && !status.isEmpty()) {
            Booking.Status bookingStatus = Booking.Status.valueOf(status.toUpperCase());
            return bookingRepository
                    .findByConsumerIdAndStatus(consumerId, bookingStatus, pageable)
                    .map(BookingResponse::new);
        } else {
            return bookingRepository
                    .findByConsumerId(consumerId, pageable)
                    .map(BookingResponse::new);
        }
    }
}
