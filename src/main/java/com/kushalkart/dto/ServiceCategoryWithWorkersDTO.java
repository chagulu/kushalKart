// ServiceCategoryWithWorkersDTO.java
package com.kushalkart.dto;

public class ServiceCategoryWithWorkersDTO {
    private Long categoryId;
    private String categoryName;
    private String userPincode;
    private long availableWorkersCount;

    public ServiceCategoryWithWorkersDTO(Long categoryId, String categoryName, 
                                        String userPincode, long availableWorkersCount) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.userPincode = userPincode;
        this.availableWorkersCount = availableWorkersCount;
    }

    // Getters and setters
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    
    public String getUserPincode() { return userPincode; }
    public void setUserPincode(String userPincode) { this.userPincode = userPincode; }
    
    public long getAvailableWorkersCount() { return availableWorkersCount; }
    public void setAvailableWorkersCount(long availableWorkersCount) { 
        this.availableWorkersCount = availableWorkersCount; 
    }
}
