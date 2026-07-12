package com.assetflow.organization.mapper;

import com.assetflow.organization.dto.response.RoleSummaryResponse;
import com.assetflow.security.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface RoleMapper {

    RoleSummaryResponse toSummaryResponse(Role role);
}
