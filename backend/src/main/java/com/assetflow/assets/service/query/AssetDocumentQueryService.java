package com.assetflow.assets.service.query;

import com.assetflow.assets.dto.response.AssetDocumentResponse;
import java.util.List;

public interface AssetDocumentQueryService {
    List<AssetDocumentResponse> findByAssetId(Long assetId);
    com.assetflow.assets.entity.AssetDocument findActiveEntityById(Long id);
}
