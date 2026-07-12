package com.assetflow.organization.service.query;

import com.assetflow.exception.ResourceNotFoundException;
import com.assetflow.organization.dto.response.DepartmentDetailResponse;
import com.assetflow.organization.dto.response.DepartmentSummaryResponse;
import com.assetflow.organization.entity.Department;
import com.assetflow.organization.mapper.DepartmentMapper;
import com.assetflow.organization.repository.DepartmentRepository;
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
class DepartmentQueryServiceImpl extends BaseQueryService<Department, Long, DepartmentRepository> implements DepartmentQueryService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    protected DepartmentRepository getRepository() {
        return departmentRepository;
    }

    @Override
    protected String getResourceName() {
        return "Department";
    }

    public Page<DepartmentSummaryResponse> findAllActive(Pageable pageable) {
        return departmentRepository.findAll((root, query, cb) -> 
            cb.equal(root.get("status"), RecordStatus.ACTIVE), pageable)
            .map(departmentMapper::toSummaryResponse);
    }

    public DepartmentDetailResponse findActiveById(Long id) {
        Department dept = findActiveEntityById(id);
        return departmentMapper.toDetailResponse(dept);
    }
}
