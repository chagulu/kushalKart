package com.kushalkart.dto;

import com.kushalkart.entity.Booking;

import java.time.LocalDateTime;

public class BookingResponse {

    private Long id;
    private Long workerId;
    private Long consumerId;
    private Long serviceId;
    private String address;
    private Booking.Status status;
    private Booking.PaymentStatus paymentStatus;
    private LocalDateTime scheduledTime;

    // Constructor
    public BookingResponse(Booking booking) {
        this.id = booking.getId();
        this.workerId = booking.getWorkerId();
        this.consumerId = booking.getConsumerId();
        this.serviceId = booking.getServiceId();
        this.address = booking.getAddress();
        this.status = booking.getStatus();
        this.paymentStatus = booking.getPaymentStatus();
        this.scheduledTime = booking.getScheduledTime();
    }

    // Getters
    public Long getId() { return id; }
    public Long getWorkerId() { return workerId; }
    public Long getConsumerId() { return consumerId; }
    public Long getServiceId() { return serviceId; }
    public String getAddress() { return address; }
    public Booking.Status getStatus() { return status; }
    public Booking.PaymentStatus getPaymentStatus() { return paymentStatus; }
    public LocalDateTime getScheduledTime() { return scheduledTime; }

    // Optionally, add setters if needed
}
