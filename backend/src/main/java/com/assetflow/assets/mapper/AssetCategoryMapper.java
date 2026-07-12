package com.assetflow.assets.mapper;

import com.assetflow.assets.dto.request.AssetCategoryRequest;
import com.assetflow.assets.dto.response.AssetCategoryDetailResponse;
import com.assetflow.assets.dto.response.AssetCategorySummaryResponse;
import com.assetflow.assets.entity.AssetCategory;
import com.assetflow.shared.mapper.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CentralMapperConfig.class)
public interface AssetCategoryMapper {

    @Mapping(target = "parentCategoryId", source = "parentCategory.id")
    @Mapping(target = "parentCategoryName", source = "parentCategory.name")
    AssetCategorySummaryResponse toSummaryResponse(AssetCategory category);

    @Mapping(target = "parentCategoryId", source = "parentCategory.id")
    @Mapping(target = "parentCategoryName", source = "parentCategory.name")
    AssetCategoryDetailResponse toDetailResponse(AssetCategory category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "parentCategory", ignore = true)
    AssetCategory toEntity(AssetCategoryRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "parentCategory", ignore = true)
    void updateEntityFromRequest(AssetCategoryRequest request, @MappingTarget AssetCategory category);
}
