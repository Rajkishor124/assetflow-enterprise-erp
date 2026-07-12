package com.assetflow.assets.mapper;

import com.assetflow.assets.dto.request.AssetDocumentRequest;
import com.assetflow.assets.dto.response.AssetDocumentResponse;
import com.assetflow.assets.entity.AssetDocument;
import com.assetflow.shared.mapper.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CentralMapperConfig.class)
public interface AssetDocumentMapper {

    @Mapping(target = "assetId", source = "asset.id")
    AssetDocumentResponse toResponse(AssetDocument document);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "asset", ignore = true)
    AssetDocument toEntity(AssetDocumentRequest request);
}
