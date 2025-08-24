package com.kushalkart.dto;

import java.io.Serializable;
import java.math.BigDecimal;
// If you globally exclude nulls and want keys to always appear, uncomment:
// import com.fasterxml.jackson.annotation.JsonInclude;
// @JsonInclude(JsonInclude.Include.ALWAYS)

/**

Summary DTO for service categories enriched with metadata.

Includes category info, user pincode echo, available workers count,

description/defaultRate from service_details, and averageRating from ratings.
*/
public class ServiceCategorySummaryDTO implements Serializable {

private static final long serialVersionUID = 1L;

private Long categoryId;
private String categoryName;
private String userPincode;
private long availableWorkersCount;
private String description;
private BigDecimal defaultRate;
private Double averageRating;

// No-args constructor for serializers (Jackson)
public ServiceCategorySummaryDTO() {
}

public ServiceCategorySummaryDTO(Long categoryId,
String categoryName,
String userPincode,
long availableWorkersCount,
String description,
BigDecimal defaultRate,
Double averageRating) {
this.categoryId = categoryId;
this.categoryName = categoryName;
this.userPincode = userPincode;
this.availableWorkersCount = availableWorkersCount;
this.description = description;
this.defaultRate = defaultRate;
this.averageRating = averageRating;
}

// Optional: static factory for readability in mappers
public static ServiceCategorySummaryDTO of(Long categoryId,
String categoryName,
String userPincode,
long availableWorkersCount,
String description,
BigDecimal defaultRate,
Double averageRating) {
return new ServiceCategorySummaryDTO(
categoryId, categoryName, userPincode, availableWorkersCount,
description, defaultRate, averageRating
);
}

public Long getCategoryId() {
return categoryId;
}
public void setCategoryId(Long categoryId) {
this.categoryId = categoryId;
}

/*
 * Backwards-compatible accessor named getId()/setId() used by some mappers/clients.
 * These simply delegate to the existing categoryId field.
 */
public Long getId() {
return categoryId;
}
public void setId(Long id) {
this.categoryId = id;
}

public String getCategoryName() {
return categoryName;
}
public void setCategoryName(String categoryName) {
this.categoryName = categoryName;
}

public String getUserPincode() {
return userPincode;
}
public void setUserPincode(String userPincode) {
this.userPincode = userPincode;
}

public long getAvailableWorkersCount() {
return availableWorkersCount;
}
public void setAvailableWorkersCount(long availableWorkersCount) {
this.availableWorkersCount = availableWorkersCount;
}

public String getDescription() {
return description;
}
public void setDescription(String description) {
this.description = description;
}

public BigDecimal getDefaultRate() {
return defaultRate;
}
public void setDefaultRate(BigDecimal defaultRate) {
this.defaultRate = defaultRate;
}

public Double getAverageRating() {
return averageRating;
}
public void setAverageRating(Double averageRating) {
this.averageRating = averageRating;
}
}