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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentQueryService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public Page<DepartmentSummaryResponse> findAllActive(Pageable pageable) {
        return departmentRepository.findAll((root, query, cb) -> 
            cb.equal(root.get("status"), RecordStatus.ACTIVE), pageable)
            .map(departmentMapper::toSummaryResponse);
    }

    public DepartmentDetailResponse findActiveById(Long id) {
        Department dept = getActiveEntityById(id);
        return departmentMapper.toDetailResponse(dept);
    }

    public Department getActiveEntityById(Long id) {
        return departmentRepository.findByIdAndStatus(id, RecordStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
    }
}
