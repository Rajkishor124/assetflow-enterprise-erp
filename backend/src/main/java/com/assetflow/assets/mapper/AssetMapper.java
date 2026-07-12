package com.assetflow.assets.mapper;

import com.assetflow.assets.dto.request.AssetRequest;
import com.assetflow.assets.dto.response.AssetDetailResponse;
import com.assetflow.assets.dto.response.AssetSummaryResponse;
import com.assetflow.assets.entity.Asset;
import com.assetflow.shared.mapper.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CentralMapperConfig.class)
public interface AssetMapper {

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.name")
    @Mapping(target = "assignedUserId", source = "assignedUser.id")
    @Mapping(target = "assignedUserName", source = "assignedUser.firstName") // Can combine later
    AssetSummaryResponse toSummaryResponse(Asset asset);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.name")
    @Mapping(target = "assignedUserId", source = "assignedUser.id")
    @Mapping(target = "assignedUserName", source = "assignedUser.firstName")
    @Mapping(target = "documents", ignore = true) // Set manually in service
    AssetDetailResponse toDetailResponse(Asset asset);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "lifecycleStatus", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "assignedUser", ignore = true)
    Asset toEntity(AssetRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "lifecycleStatus", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "assignedUser", ignore = true)
    @Mapping(target = "assetTag", ignore = true) // Asset tag usually shouldn't change
    void updateEntityFromRequest(AssetRequest request, @MappingTarget Asset asset);
}
