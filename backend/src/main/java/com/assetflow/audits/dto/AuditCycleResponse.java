package com.assetflow.audits.dto;

import com.assetflow.audits.enums.AuditStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class AuditCycleResponse {
    private Long id;
    
    private String name;
    private String description;
    
    private Instant startDate;
    private Instant endDate;
    
    private AuditStatus auditStatus;
    
    private Long initiatedById;
    private String initiatedByName;
    
    private Instant createdAt;
    private Instant updatedAt;
}
