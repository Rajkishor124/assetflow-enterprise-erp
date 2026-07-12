package com.assetflow.organization.service.command;

import com.assetflow.exception.DuplicateResourceException;
import com.assetflow.organization.dto.request.DepartmentRequest;
import com.assetflow.organization.entity.Department;
import com.assetflow.organization.mapper.DepartmentMapper;
import com.assetflow.organization.repository.DepartmentRepository;
import com.assetflow.organization.service.query.DepartmentQueryService;
import com.assetflow.organization.validation.DepartmentHierarchyValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentCommandServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private DepartmentMapper departmentMapper;
    @Mock
    private DepartmentQueryService departmentQueryService;
    @Mock
    private DepartmentHierarchyValidator hierarchyValidator;

    @InjectMocks
    private DepartmentCommandService departmentCommandService;

    @Test
    void createDepartment_ShouldThrowDuplicateResourceException_WhenNameExistsWithoutParent() {
        DepartmentRequest request = new DepartmentRequest();
        request.setName("HR");

        when(departmentRepository.existsByNameAndParentIsNull("HR")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> departmentCommandService.createDepartment(request));
        
        verify(departmentRepository, never()).save(any(Department.class));
    }
}
