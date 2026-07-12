package com.assetflow.organization.service.command;

import com.assetflow.organization.dto.response.DepartmentDetailResponse;
import com.assetflow.organization.dto.request.DepartmentRequest;

public interface DepartmentCommandService {
    DepartmentDetailResponse createDepartment(DepartmentRequest request);
    DepartmentDetailResponse updateDepartment(Long id, DepartmentRequest request);
    void deleteDepartment(Long id);
}
