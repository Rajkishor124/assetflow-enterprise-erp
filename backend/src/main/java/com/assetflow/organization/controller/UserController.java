package com.assetflow.organization.controller;

import com.assetflow.organization.dto.request.UserRequest;
import com.assetflow.organization.dto.response.UserDetailResponse;
import com.assetflow.organization.dto.response.UserSummaryResponse;
import com.assetflow.organization.service.command.UserCommandService;
import com.assetflow.organization.service.query.UserQueryService;
import com.assetflow.shared.dto.ApiResponse;
import com.assetflow.util.ApiPaths;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(ApiPaths.USERS)
@RequiredArgsConstructor
public class UserController {

    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ASSET_MANAGER', 'DEPT_HEAD')")
    public ApiResponse<Page<UserSummaryResponse>> getAllUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Long deptId,
            Pageable pageable) {
        Page<UserSummaryResponse> users = userQueryService.findAllActive(role, deptId, pageable);
        return ApiResponse.success(users, "Users retrieved successfully");
    }

    @GetMapping("/{id}")
    public ApiResponse<UserDetailResponse> getUserById(@PathVariable Long id) {
        UserDetailResponse user = userQueryService.findActiveById(id);
        return ApiResponse.success(user, "User details retrieved successfully");
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserDetailResponse> updateUserRole(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String roleName = body.get("role");
        UserDetailResponse user = userCommandService.updateUserRole(id, roleName);
        return ApiResponse.success(user, "User role updated successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserDetailResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        UserDetailResponse user = userCommandService.updateUser(id, request);
        return ApiResponse.success(user, "User updated successfully");
    }
}
