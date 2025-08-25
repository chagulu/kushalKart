package com.kushalkart.dto;

import java.time.LocalDateTime;

public class TransactionResponse {

    private Long id;
    private Long userId;
    private Double amount;
    private String transactionType;
    private LocalDateTime createdAt;
    private String remarks;
    private String transactionStatus;
    private String paymentType;
    private String transactionReference;
    private String balanceType;

    // ✅ No-arg constructor (required by frameworks)
    public TransactionResponse() {
    }

    // ✅ Full parameterized constructor
    public TransactionResponse(Long id, Long userId, Double amount, String transactionType,
                               LocalDateTime createdAt, String remarks, String transactionStatus,
                               String paymentType, String transactionReference, String balanceType) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.createdAt = createdAt;
        this.remarks = remarks;
        this.transactionStatus = transactionStatus;
        this.paymentType = paymentType;
        this.transactionReference = transactionReference;
        this.balanceType = balanceType;
    }

    // ✅ Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }
}
