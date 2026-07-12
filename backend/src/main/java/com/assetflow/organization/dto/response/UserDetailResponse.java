package com.assetflow.organization.dto.response;

import com.assetflow.shared.enums.RecordStatus;
import lombok.Data;

import java.time.Instant;

@Data
public class UserDetailResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private DepartmentSummaryResponse department;
    private RoleSummaryResponse role;
    private RecordStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private Long version;
}
