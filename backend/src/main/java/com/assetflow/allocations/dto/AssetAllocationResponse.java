package com.assetflow.allocations.dto;

import com.assetflow.allocations.enums.AllocationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class AssetAllocationResponse {
    private Long id;
    private Long assetId;
    private String assetName;
    private String assetTag;
    
    private Long userId;
    private String userName;
    
    private Long departmentId;
    private String departmentName;

    private Instant allocationDate;
    private Instant expectedReturnDate;
    private Instant actualReturnDate;
    private AllocationStatus allocationStatus;
    private String notes;
}
