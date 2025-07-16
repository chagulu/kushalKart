package com.kushalkart.dto;

public class ServiceCategoryDetailsDTO {
    private Long categoryId;
    private String categoryName;
    private String name;

    public ServiceCategoryDetailsDTO() {}

    public ServiceCategoryDetailsDTO(Long categoryId, String categoryName, String name) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.name = name;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
