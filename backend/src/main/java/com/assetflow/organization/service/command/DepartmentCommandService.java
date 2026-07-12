package com.assetflow.organization.service.command;

import com.assetflow.exception.DuplicateResourceException;
import com.assetflow.organization.dto.request.DepartmentRequest;
import com.assetflow.organization.dto.response.DepartmentDetailResponse;
import com.assetflow.organization.entity.Department;
import com.assetflow.organization.mapper.DepartmentMapper;
import com.assetflow.organization.repository.DepartmentRepository;
import com.assetflow.organization.service.query.DepartmentQueryService;
import com.assetflow.organization.validation.DepartmentHierarchyValidator;
import com.assetflow.shared.enums.RecordStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentCommandService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final DepartmentQueryService departmentQueryService;
    private final DepartmentHierarchyValidator hierarchyValidator;

    public DepartmentDetailResponse createDepartment(DepartmentRequest request) {
        if (request.getParentId() != null) {
            if (departmentRepository.existsByNameAndParentId(request.getName(), request.getParentId())) {
                throw new DuplicateResourceException("Department", "name and parentId", request.getName());
            }
        } else {
            if (departmentRepository.existsByNameAndParentIsNull(request.getName())) {
                throw new DuplicateResourceException("Department", "name", request.getName());
            }
        }

        Department department = departmentMapper.toEntity(request);

        if (request.getParentId() != null) {
            Department parent = departmentQueryService.getActiveEntityById(request.getParentId());
            department.setParent(parent);
        }

        Department saved = departmentRepository.save(department);
        return departmentMapper.toDetailResponse(saved);
    }

    public DepartmentDetailResponse updateDepartment(Long id, DepartmentRequest request) {
        Department department = departmentQueryService.getActiveEntityById(id);

        if (request.getParentId() != null) {
            hierarchyValidator.validateNotSelfParent(id, request.getParentId());
        }

        departmentMapper.updateEntityFromRequest(request, department);

        if (request.getParentId() != null) {
            Department parent = departmentQueryService.getActiveEntityById(request.getParentId());
            department.setParent(parent);
        } else {
            department.setParent(null);
        }

        Department saved = departmentRepository.save(department);
        return departmentMapper.toDetailResponse(saved);
    }

    public void deleteDepartment(Long id) {
        Department department = departmentQueryService.getActiveEntityById(id);
        department.setStatus(RecordStatus.INACTIVE);
        departmentRepository.save(department);
    }
}
