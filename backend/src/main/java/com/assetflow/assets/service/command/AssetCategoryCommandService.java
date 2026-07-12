package com.assetflow.assets.service.command;

import com.assetflow.assets.dto.request.AssetCategoryRequest;
import com.assetflow.assets.dto.response.AssetCategoryDetailResponse;

public interface AssetCategoryCommandService {
    AssetCategoryDetailResponse createCategory(AssetCategoryRequest request);
    AssetCategoryDetailResponse updateCategory(Long id, AssetCategoryRequest request);
    void deleteCategory(Long id);
}
