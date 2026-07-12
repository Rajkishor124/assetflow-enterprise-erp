package com.assetflow.organization.dto.response;

import lombok.Data;

@Data
public class UserSummaryResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String departmentName;
    private String roleName;
}
