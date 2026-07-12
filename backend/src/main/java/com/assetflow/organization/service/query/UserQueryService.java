package com.assetflow.organization.service.query;

import com.assetflow.organization.dto.response.UserDetailResponse;
import com.assetflow.organization.dto.response.UserSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserQueryService {
    Page<UserSummaryResponse> findAllActive(String role, Long deptId, Pageable pageable);
    UserDetailResponse findActiveById(Long id);
    com.assetflow.organization.entity.User findActiveEntityById(Long id);
}
