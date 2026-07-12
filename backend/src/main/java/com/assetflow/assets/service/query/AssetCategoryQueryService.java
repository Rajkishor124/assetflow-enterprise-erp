package com.assetflow.assets.service.query;

import com.assetflow.assets.dto.response.AssetCategoryDetailResponse;
import com.assetflow.assets.dto.response.AssetCategorySummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AssetCategoryQueryService {
    Page<AssetCategorySummaryResponse> findAllActive(Pageable pageable);
    AssetCategoryDetailResponse findActiveById(Long id);
    com.assetflow.assets.entity.AssetCategory findActiveEntityById(Long id);
}
