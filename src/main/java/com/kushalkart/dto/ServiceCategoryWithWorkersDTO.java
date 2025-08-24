package com.kushalkart.dto;

import java.math.BigDecimal;

public class ServiceCategoryWithWorkersDTO {

    private Long categoryId;
    private String categoryName;
    private String userPincode;
    private long availableWorkersCount;
    private String description;
    private BigDecimal defaultRate;
    private Double averageRating;

    public ServiceCategoryWithWorkersDTO(Long categoryId, String categoryName,
                                         String userPincode, long availableWorkersCount) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.userPincode = userPincode;
        this.availableWorkersCount = availableWorkersCount;
    }

    // New full constructor with enriched fields
    public ServiceCategoryWithWorkersDTO(Long categoryId, String categoryName, String userPincode,
                                         long availableWorkersCount, String description,
                                         BigDecimal defaultRate, Double averageRating) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.userPincode = userPincode;
        this.availableWorkersCount = availableWorkersCount;
        this.description = description;
        this.defaultRate = defaultRate;
        this.averageRating = averageRating;
    }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getUserPincode() { return userPincode; }
    public void setUserPincode(String userPincode) { this.userPincode = userPincode; }

    public long getAvailableWorkersCount() { return availableWorkersCount; }
    public void setAvailableWorkersCount(long availableWorkersCount) { this.availableWorkersCount = availableWorkersCount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getDefaultRate() { return defaultRate; }
    public void setDefaultRate(BigDecimal defaultRate) { this.defaultRate = defaultRate; }

    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }

}

