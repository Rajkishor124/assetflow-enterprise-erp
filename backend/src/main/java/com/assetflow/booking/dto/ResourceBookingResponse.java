package com.assetflow.booking.dto;

import com.assetflow.booking.enums.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ResourceBookingResponse {
    private Long id;
    
    private Long resourceId;
    private String resourceName;
    private String resourceType;
    
    private Long userId;
    private String userName;
    
    private Instant startTime;
    private Instant endTime;
    
    private BookingStatus bookingStatus;
    private String purpose;
}
