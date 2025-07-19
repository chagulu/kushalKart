package com.kushalkart.admin.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "worker_kyc")
public class WorkerKyc {

    public enum Status {
        PENDING, VERIFIED, REJECTED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "worker_id", nullable = false)
    private Worker worker;

    @Column(name = "aadhaar_number", nullable = false)
    private String aadhaarNumber;

    @Column(name = "aadhaar_front_path", nullable = false)
    private String aadhaarFrontPath;

    @Column(name = "aadhaar_back_path", nullable = false)
    private String aadhaarBackPath;

    @Column(name = "worker_photo_path", nullable = false)
    private String workerPhotoPath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    @ManyToOne
    @JoinColumn(name = "registered_by", nullable = false)
    private AdminUser registeredBy;

    @ManyToOne
    @JoinColumn(name = "verified_by")
    private AdminUser verifiedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // getters & setters …
    // include verifiedBy

    public Long getId() {
        return id;
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

    public AdminUser getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(AdminUser verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
