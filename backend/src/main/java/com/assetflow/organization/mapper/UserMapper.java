package com.assetflow.organization.mapper;

import com.assetflow.organization.dto.request.UserRequest;
import com.assetflow.organization.dto.response.UserDetailResponse;
import com.assetflow.organization.dto.response.UserSummaryResponse;
import com.assetflow.organization.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.assetflow.shared.mapper.CentralMapperConfig;

@Mapper(config = CentralMapperConfig.class, uses = {DepartmentMapper.class, RoleMapper.class})
public interface UserMapper {

    @Mapping(target = "departmentName", source = "department.name")
    @Mapping(target = "roleName", source = "role.name")
    UserSummaryResponse toSummaryResponse(User user);

    UserDetailResponse toDetailResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true) // Handled by service
    @Mapping(target = "department", ignore = true)   // Handled by service
    @Mapping(target = "role", ignore = true)         // Handled by service
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    User toEntity(UserRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true) // Handled by service
    @Mapping(target = "department", ignore = true)   // Handled by service
    @Mapping(target = "role", ignore = true)         // Handled by service
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromRequest(UserRequest request, @MappingTarget User user);
}
