package com.assetflow.assets.service.command;

import com.assetflow.assets.dto.request.AssetRequest;
import com.assetflow.assets.dto.response.AssetDetailResponse;

public interface AssetCommandService {
    AssetDetailResponse createAsset(AssetRequest request);
    AssetDetailResponse updateAssetDetails(Long id, AssetRequest request);
    
    // Intent-based methods for state transitions
    void allocateAsset(Long id, Long userId, Long departmentId);
    void returnAsset(Long id);
    void retireAsset(Long id);
    void markLost(Long id);
    void sendToMaintenance(Long id);
    void restoreFromMaintenance(Long id);
}
