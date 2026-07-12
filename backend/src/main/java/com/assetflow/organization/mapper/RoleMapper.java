package com.assetflow.organization.mapper;

import com.assetflow.organization.dto.response.RoleSummaryResponse;
import com.assetflow.security.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.assetflow.shared.mapper.CentralMapperConfig;

@Mapper(config = CentralMapperConfig.class)
public interface RoleMapper {

    RoleSummaryResponse toSummaryResponse(Role role);
}
