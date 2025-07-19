package com.kushalkart.admin.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "worker_kyc")
public class WorkerKyc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", nullable = false)
    private Worker worker;

    @Column(nullable = false)
    private String aadhaarNumber;

    @Column(nullable = false)
    private String aadhaarFrontPath;

    @Column(nullable = false)
    private String aadhaarBackPath;

    @Column(nullable = false)
    private String workerPhotoPath;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registered_by")
    private AdminUser registeredBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public enum Status {
        PENDING,
        VERIFIED,
        REJECTED
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public String getAadhaarFrontPath() {
        return aadhaarFrontPath;
    }

    public void setAadhaarFrontPath(String aadhaarFrontPath) {
        this.aadhaarFrontPath = aadhaarFrontPath;
    }

    public String getAadhaarBackPath() {
        return aadhaarBackPath;
    }

    public void setAadhaarBackPath(String aadhaarBackPath) {
        this.aadhaarBackPath = aadhaarBackPath;
    }

    public String getWorkerPhotoPath() {
        return workerPhotoPath;
    }

    public void setWorkerPhotoPath(String workerPhotoPath) {
        this.workerPhotoPath = workerPhotoPath;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public AdminUser getRegisteredBy() {
        return registeredBy;
    }

    public void setRegisteredBy(AdminUser registeredBy) {
        this.registeredBy = registeredBy;
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
