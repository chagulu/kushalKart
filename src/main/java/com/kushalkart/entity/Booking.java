package com.kushalkart.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "consumer_id", nullable = false)
    private Long consumerId;

    @Column(name = "worker_id", nullable = false)
    private Long workerId;

    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "service")
    private String service;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "scheduled_time", nullable = false)
    private LocalDateTime scheduledTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "consumer_name")
    private String consumerName;

    @Column(name = "consumer_phone")
    private String consumerPhone;

    @Column(name = "consumer_email")
    private String consumerEmail;

    @Column(name = "address")
    private String address;

    @Column(name = "booking_mode")
    private String bookingMode = "APP";

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public enum Status {
        PENDING, CONFIRMED, COMPLETED, CANCELLED
    }

    public enum PaymentStatus {
        PENDING, PAID, FAILED
    }

    // ===== Getters and Setters =====

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getConsumerId() {
        return consumerId;
    }
    public void setConsumerId(Long consumerId) {
        this.consumerId = consumerId;
    }

    public Long getWorkerId() {
        return workerId;
    }
    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public Long getServiceId() {
        return serviceId;
    }
    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getService() {
        return service;
    }
    public void setService(String service) {
        this.service = service;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }
    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }
    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getConsumerName() {
        return consumerName;
    }
    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getConsumerPhone() {
        return consumerPhone;
    }
    public void setConsumerPhone(String consumerPhone) {
        this.consumerPhone = consumerPhone;
    }

    public String getConsumerEmail() {
        return consumerEmail;
    }
    public void setConsumerEmail(String consumerEmail) {
        this.consumerEmail = consumerEmail;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getBookingMode() {
        return bookingMode;
    }
    public void setBookingMode(String bookingMode) {
        this.bookingMode = bookingMode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
