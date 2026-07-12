package com.assetflow.organization.dto.response;

import com.assetflow.shared.enums.RecordStatus;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class DepartmentDetailResponse {
    private Long id;
    private String name;
    private DepartmentSummaryResponse parent;
    private List<DepartmentSummaryResponse> children;
    private RecordStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private Long version;
}
