package com.assetflow.maintenance.dto;

import com.assetflow.maintenance.enums.MaintenancePriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class MaintenanceRecordRequest {
    
    @NotNull(message = "Asset ID is required")
    private Long assetId;
    
    @NotNull(message = "Reported By User ID is required")
    private Long reportedById;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    private MaintenancePriority priority = MaintenancePriority.MEDIUM;
    
    private Instant scheduledDate;
}
