package com.kushalkart.repository;

import com.kushalkart.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;



public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    // For default non-paginated fallback use
    List<Booking> findByConsumerId(Long consumerId);
    List<Booking> findByWorkerId(Long workerId);

    // New: Paginated bookings for consumer
    Page<Booking> findByConsumerId(Long consumerId, Pageable pageable);
    List<Booking> findAllByConsumerIdOrderByScheduledTimeDesc(Long consumerId);


    // New: Paginated bookings with status filter
    Page<Booking> findByConsumerIdAndStatus(Long consumerId, Booking.Status status, Pageable pageable);
}
