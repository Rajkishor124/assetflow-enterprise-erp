package com.assetflow.organization.service.command;

import com.assetflow.organization.dto.response.UserDetailResponse;
import com.assetflow.organization.dto.request.UserRequest;

public interface UserCommandService {
    UserDetailResponse createUser(UserRequest request);
    UserDetailResponse updateUser(Long id, UserRequest request);
    UserDetailResponse updateUserRole(Long id, String roleName);
    void deleteUser(Long id);
}
