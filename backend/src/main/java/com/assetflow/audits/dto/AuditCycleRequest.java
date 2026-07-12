package com.assetflow.audits.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class AuditCycleRequest {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Start date is required")
    private Instant startDate;
    
    private Instant endDate;
    
    @NotNull(message = "Initiated by user ID is required")
    private Long initiatedById;
}
