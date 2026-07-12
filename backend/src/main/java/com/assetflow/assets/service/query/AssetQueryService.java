package com.assetflow.assets.service.query;

import com.assetflow.assets.dto.response.AssetDetailResponse;
import com.assetflow.assets.dto.response.AssetSummaryResponse;
import com.assetflow.assets.enums.AssetStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AssetQueryService {
    Page<AssetSummaryResponse> findAllActive(Long categoryId, AssetStatus status, Long assignedTo, String search, Pageable pageable);
    AssetDetailResponse findActiveById(Long id);
    com.assetflow.assets.entity.Asset findActiveEntityById(Long id);
}
