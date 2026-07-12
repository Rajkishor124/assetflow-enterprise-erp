package com.assetflow.organization.service.query;

import com.assetflow.organization.dto.response.RoleSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleQueryService {
    Page<RoleSummaryResponse> findAllActive(Pageable pageable);
    com.assetflow.security.entity.Role findActiveEntityById(Long id);
}
