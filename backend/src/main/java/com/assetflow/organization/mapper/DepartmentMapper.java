package com.assetflow.organization.mapper;

import com.assetflow.organization.dto.request.DepartmentRequest;
import com.assetflow.organization.dto.response.DepartmentDetailResponse;
import com.assetflow.organization.dto.response.DepartmentSummaryResponse;
import com.assetflow.organization.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DepartmentMapper {

    @Mapping(target = "parentId", source = "parent.id")
    DepartmentSummaryResponse toSummaryResponse(Department department);

    DepartmentDetailResponse toDetailResponse(Department department);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true) // Will be set by service
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    Department toEntity(DepartmentRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true) // Will be set by service
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromRequest(DepartmentRequest request, @MappingTarget Department department);
}
