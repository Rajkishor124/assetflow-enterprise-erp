package com.assetflow.organization.controller;

import com.assetflow.organization.dto.request.DepartmentRequest;
import com.assetflow.organization.dto.response.DepartmentDetailResponse;
import com.assetflow.organization.dto.response.DepartmentSummaryResponse;
import com.assetflow.organization.service.command.DepartmentCommandService;
import com.assetflow.organization.service.query.DepartmentQueryService;
import com.assetflow.shared.dto.ApiResponse;
import com.assetflow.util.ApiPaths;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPaths.DEPARTMENTS)
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentQueryService departmentQueryService;
    private final DepartmentCommandService departmentCommandService;

    @GetMapping
    public ApiResponse<Page<DepartmentSummaryResponse>> getAllDepartments(Pageable pageable) {
        Page<DepartmentSummaryResponse> depts = departmentQueryService.findAllActive(pageable);
        return ApiResponse.success(depts, "Departments retrieved successfully");
    }

    @GetMapping("/{id}")
    public ApiResponse<DepartmentDetailResponse> getDepartmentById(@PathVariable Long id) {
        DepartmentDetailResponse dept = departmentQueryService.findActiveById(id);
        return ApiResponse.success(dept, "Department details retrieved successfully");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<DepartmentDetailResponse> createDepartment(@Valid @RequestBody DepartmentRequest request) {
        DepartmentDetailResponse dept = departmentCommandService.createDepartment(request);
        return ApiResponse.success(dept, "Department created successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<DepartmentDetailResponse> updateDepartment(@PathVariable Long id, @Valid @RequestBody DepartmentRequest request) {
        DepartmentDetailResponse dept = departmentCommandService.updateDepartment(id, request);
        return ApiResponse.success(dept, "Department updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteDepartment(@PathVariable Long id) {
        departmentCommandService.deleteDepartment(id);
        return ApiResponse.success(null, "Department deleted successfully");
    }
}
