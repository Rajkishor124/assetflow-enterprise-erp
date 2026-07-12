package com.assetflow.organization.dto.response;

import lombok.Data;

@Data
public class DepartmentSummaryResponse {
    private Long id;
    private String name;
    private Long parentId;
}
