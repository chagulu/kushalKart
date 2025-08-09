package com.kushalkart.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import jakarta.persistence.*;

@Entity
@Table(name = "master_transaction_history")
public class MasterTransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "booking_id")
    private Long bookingId;

    @Column(name = "balance_type_id", nullable = false)
    private Long balanceTypeId;

    @Column(name = "transaction_type_id", nullable = false)
    private Long transactionTypeId;

    @Column(name = "payment_type_id")
    private Long paymentTypeId;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "transaction_reference")
    private String transactionReference;

    @Column(name = "transaction_status_id", nullable = false)
    private Long transactionStatusId;

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    // Optional: If you want to keep updatedAt as well.
    // @Column(name = "updated_at")
    // private Timestamp updatedAt;

    // ==== Constructors ====
    public MasterTransactionHistory() {
        // Required by JPA
    }

    // ==== Getters and Setters ====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public Long getBalanceTypeId() { return balanceTypeId; }
    public void setBalanceTypeId(Long balanceTypeId) { this.balanceTypeId = balanceTypeId; }

    public Long getTransactionTypeId() { return transactionTypeId; }
    public void setTransactionTypeId(Long transactionTypeId) { this.transactionTypeId = transactionTypeId; }

    public Long getPaymentTypeId() { return paymentTypeId; }
    public void setPaymentTypeId(Long paymentTypeId) { this.paymentTypeId = paymentTypeId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getTransactionReference() { return transactionReference; }
    public void setTransactionReference(String transactionReference) { this.transactionReference = transactionReference; }

    public Long getTransactionStatusId() { return transactionStatusId; }
    public void setTransactionStatusId(Long transactionStatusId) { this.transactionStatusId = transactionStatusId; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    // If you want updatedAt:
    // public Timestamp getUpdatedAt() { return updatedAt; }
    // public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    // ==== JPA Lifecycle Callbacks ====
    @PrePersist
    protected void onCreate() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    // If you want updatedAt:
    // @PreUpdate
    // protected void onUpdate() {
    //     this.updatedAt = new Timestamp(System.currentTimeMillis());
    // }
}
