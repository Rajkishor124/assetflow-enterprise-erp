package com.assetflow.assets.service.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// Using raw entity for now until DTOs are created
public interface AssetQueryService {
    com.assetflow.assets.entity.Asset findActiveEntityById(Long id);
}
