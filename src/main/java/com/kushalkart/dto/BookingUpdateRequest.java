// BookingUpdateRequest.java
package com.kushalkart.dto;

import java.time.LocalDateTime;

public class BookingUpdateRequest {
    private LocalDateTime scheduledTime;
    private String action; // "reschedule" or "cancel"

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
