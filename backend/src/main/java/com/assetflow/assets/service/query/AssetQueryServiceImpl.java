package com.assetflow.assets.service.query;

import com.assetflow.assets.entity.Asset;
import com.assetflow.assets.repository.AssetRepository;
import com.assetflow.shared.service.BaseQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class AssetQueryServiceImpl extends BaseQueryService<Asset, Long, AssetRepository> implements AssetQueryService {

    private final AssetRepository assetRepository;

    @Override
    protected AssetRepository getRepository() {
        return assetRepository;
    }

    @Override
    protected String getResourceName() {
        return "Asset";
    }
}
