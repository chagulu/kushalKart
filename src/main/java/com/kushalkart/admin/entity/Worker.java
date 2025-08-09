package com.kushalkart.admin.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "workers")
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String username;
    private String mobile; // Stored field
    private String email;
    private String password;
    private String service;
    private String bio;
    private String skills;

    @Column(name = "rate_per_hour")
    private BigDecimal ratePerHour;

    @Enumerated(EnumType.STRING)
    private KycStatus kycStatus;

    @Column(name = "service_category_id")
    private Long serviceCategoryId;

    @Column(name = "service_id")
    private Long serviceId;

    private boolean verified;

    // ----- ENUM -----
    public enum KycStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

    // ----- GETTERS AND SETTERS -----
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    // ✅ Added to support getMobileNo() used in service code
    public String getMobileNo() {
        return this.mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public BigDecimal getRatePerHour() {
        return ratePerHour;
    }

    public void setRatePerHour(BigDecimal ratePerHour) {
        this.ratePerHour = ratePerHour;
    }

    public KycStatus getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(KycStatus kycStatus) {
        this.kycStatus = kycStatus;
    }

    public Long getServiceCategoryId() {
        return serviceCategoryId;
    }

    public void setServiceCategoryId(Long serviceCategoryId) {
        this.serviceCategoryId = serviceCategoryId;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    // ✅ Newly added getter/setter for serviceId
    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
    return this.service;
}
}
