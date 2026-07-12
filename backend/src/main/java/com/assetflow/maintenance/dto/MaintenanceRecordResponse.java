package com.assetflow.maintenance.dto;

import com.assetflow.maintenance.enums.MaintenancePriority;
import com.assetflow.maintenance.enums.MaintenanceStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class MaintenanceRecordResponse {
    private Long id;
    
    private Long assetId;
    private String assetName;
    private String assetTag;
    
    private Long reportedById;
    private String reportedByName;
    
    private String title;
    private String description;
    
    private MaintenancePriority priority;
    private MaintenanceStatus maintenanceStatus;
    
    private Instant scheduledDate;
    private Instant resolutionDate;
    
    private Instant createdAt;
    private Instant updatedAt;
}
