package com.assetflow.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class ResourceBookingRequest {
    
    @NotNull(message = "Resource ID is required")
    private Long resourceId;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Start time is required")
    private Instant startTime;
    
    @NotNull(message = "End time is required")
    private Instant endTime;
    
    private String purpose;
}
