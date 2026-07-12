package com.assetflow.assets.service.query;

import com.assetflow.assets.dto.response.AssetCategoryDetailResponse;
import com.assetflow.assets.dto.response.AssetCategorySummaryResponse;
import com.assetflow.assets.entity.AssetCategory;
import com.assetflow.assets.mapper.AssetCategoryMapper;
import com.assetflow.assets.repository.AssetCategoryRepository;
import com.assetflow.shared.enums.RecordStatus;
import com.assetflow.shared.service.BaseQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class AssetCategoryQueryServiceImpl extends BaseQueryService<AssetCategory, Long, AssetCategoryRepository> implements AssetCategoryQueryService {

    private final AssetCategoryRepository assetCategoryRepository;
    private final AssetCategoryMapper assetCategoryMapper;

    @Override
    protected AssetCategoryRepository getRepository() {
        return assetCategoryRepository;
    }

    @Override
    protected String getResourceName() {
        return "AssetCategory";
    }

    @Override
    public Page<AssetCategorySummaryResponse> findAllActive(Pageable pageable) {
        return assetCategoryRepository.findAll((root, query, cb) -> 
            cb.equal(root.get("status"), RecordStatus.ACTIVE), pageable)
            .map(assetCategoryMapper::toSummaryResponse);
    }

    @Override
    public AssetCategoryDetailResponse findActiveById(Long id) {
        AssetCategory category = findActiveEntityById(id);
        return assetCategoryMapper.toDetailResponse(category);
    }
}
