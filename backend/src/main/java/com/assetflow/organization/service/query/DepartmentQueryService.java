package com.assetflow.organization.service.query;

import com.assetflow.organization.dto.response.DepartmentDetailResponse;
import com.assetflow.organization.dto.response.DepartmentSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentQueryService {
    Page<DepartmentSummaryResponse> findAllActive(Pageable pageable);
    DepartmentDetailResponse findActiveById(Long id);
    // Needed by other services:
    com.assetflow.organization.entity.Department findActiveEntityById(Long id);
}
