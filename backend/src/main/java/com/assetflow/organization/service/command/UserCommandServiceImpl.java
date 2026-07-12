package com.assetflow.organization.service.command;

import com.assetflow.exception.DuplicateResourceException;
import com.assetflow.organization.dto.request.UserRequest;
import com.assetflow.organization.dto.response.UserDetailResponse;
import com.assetflow.organization.entity.Department;
import com.assetflow.organization.entity.User;
import com.assetflow.organization.mapper.UserMapper;
import com.assetflow.organization.repository.UserRepository;
import com.assetflow.organization.service.query.DepartmentQueryService;
import com.assetflow.organization.service.query.RoleQueryService;
import com.assetflow.organization.service.query.UserQueryService;
import com.assetflow.security.entity.Role;
import com.assetflow.shared.enums.RecordStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.assetflow.shared.service.BaseCommandService;

@Service
@RequiredArgsConstructor
@Transactional
class UserCommandServiceImpl extends BaseCommandService<User, Long, UserRepository> implements UserCommandService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserQueryService userQueryService;
    private final DepartmentQueryService departmentQueryService;
    private final RoleQueryService roleQueryService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected UserRepository getRepository() {
        return userRepository;
    }

    @Override
    protected String getResourceName() {
        return "User";
    }

    public UserDetailResponse createUser(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }

        User user = userMapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        if (request.getDepartmentId() != null) {
            Department department = departmentQueryService.findActiveEntityById(request.getDepartmentId());
            user.setDepartment(department);
        }

        Role role = roleQueryService.findActiveEntityById(request.getRoleId());
        user.setRole(role);

        User saved = userRepository.save(user);
        return userMapper.toDetailResponse(saved);
    }

    public UserDetailResponse updateUser(Long id, UserRequest request) {
        User user = userQueryService.findActiveEntityById(id);

        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }

        userMapper.updateEntityFromRequest(request, user);

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getDepartmentId() != null) {
            Department department = departmentQueryService.findActiveEntityById(request.getDepartmentId());
            user.setDepartment(department);
        } else {
            user.setDepartment(null);
        }

        Role role = roleQueryService.findActiveEntityById(request.getRoleId());
        user.setRole(role);

        User saved = userRepository.save(user);
        return userMapper.toDetailResponse(saved);
    }

    public void deleteUser(Long id) {
        User user = userQueryService.findActiveEntityById(id);
        user.setStatus(RecordStatus.INACTIVE);
        userRepository.save(user);
    }
}
