package com.assetflow.allocations.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class AssetAllocationRequest {
    @NotNull(message = "Asset ID is required")
    private Long assetId;

    private Long userId;
    private Long departmentId;

    @NotNull(message = "Allocation date is required")
    private Instant allocationDate;

    private Instant expectedReturnDate;
    private String notes;
}
