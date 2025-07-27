package com.kushalkart.controller;

import com.kushalkart.dto.BookingRequest;
import com.kushalkart.dto.BookingUpdateRequest;
import com.kushalkart.entity.Booking;
import com.kushalkart.service.BookingService;
import com.kushalkart.model.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> bookWorker(@RequestBody BookingRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getUserId(); // Extracted from authenticated principal

            Booking booking = bookingService.createBooking(userId, request);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Something went wrong: " + e.getMessage(), "error")
            );
        }
    }

    // Error response class
    public static class ErrorResponse {
        private String message;
        private String status;

        public ErrorResponse(String message, String status) {
            this.message = message;
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public String getStatus() {
            return status;
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

}
