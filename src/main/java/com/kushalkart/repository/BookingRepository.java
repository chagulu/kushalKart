package com.kushalkart.repository;

import com.kushalkart.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByConsumerId(Long consumerId);

    List<Booking> findByWorkerId(Long workerId);

    // For "My Bookings" view - most recent first
    List<Booking> findAllByConsumerIdOrderByScheduledTimeDesc(Long consumerId);
}
