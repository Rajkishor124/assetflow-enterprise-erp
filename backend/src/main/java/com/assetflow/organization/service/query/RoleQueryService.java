package com.assetflow.organization.service.query;

import com.assetflow.exception.ResourceNotFoundException;
import com.assetflow.organization.dto.response.RoleSummaryResponse;
import com.assetflow.organization.mapper.RoleMapper;
import com.assetflow.security.entity.Role;
import com.assetflow.security.repository.RoleRepository;
import com.assetflow.shared.enums.RecordStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.assetflow.shared.service.BaseQueryService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleQueryService extends BaseQueryService<Role, Long, RoleRepository> {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    protected RoleRepository getRepository() {
        return roleRepository;
    }

    @Override
    protected String getResourceName() {
        return "Role";
    }

    public Page<RoleSummaryResponse> findAllActive(Pageable pageable) {
        return roleRepository.findAll((root, query, cb) -> 
            cb.equal(root.get("status"), RecordStatus.ACTIVE), pageable)
            .map(roleMapper::toSummaryResponse);
    }
}
