package com.kushalkart.service;

import com.kushalkart.dto.BookingRequest;
import com.kushalkart.dto.BookingUpdateRequest;
import com.kushalkart.entity.Booking;
import com.kushalkart.entity.User;
import com.kushalkart.admin.entity.Worker;
import com.kushalkart.repository.BookingRepository;
import com.kushalkart.repository.UserRepository;
import com.kushalkart.admin.repository.WorkerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Booking createBooking(Long consumerId, BookingRequest dto) {
        Booking booking = new Booking();
        booking.setConsumerId(consumerId);
        booking.setWorkerId(dto.getWorkerId());
        booking.setScheduledTime(dto.getScheduledTime());
        booking.setStatus(Booking.Status.PENDING);
        booking.setPaymentStatus(Booking.PaymentStatus.PENDING);

        // Set service ID from request
        booking.setServiceId(dto.getServiceId());

        // Fetch address from consumer
        User user = userRepository.findById(consumerId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getAddress() != null) {
            booking.setAddress(user.getAddress().getFullAddress());
        } else {
            booking.setAddress(null);
        }

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

    public List<Booking> getBookingsForConsumer(Long consumerId) {
        return bookingRepository.findAllByConsumerIdOrderByScheduledTimeDesc(consumerId);
    }
}
