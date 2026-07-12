package com.assetflow.assets.service.query;

import com.assetflow.assets.dto.response.AssetDetailResponse;
import com.assetflow.assets.dto.response.AssetSummaryResponse;
import com.assetflow.assets.entity.Asset;
import com.assetflow.assets.enums.AssetStatus;
import com.assetflow.assets.mapper.AssetMapper;
import com.assetflow.assets.repository.AssetRepository;
import com.assetflow.assets.specification.AssetSpecification;
import com.assetflow.core.service.CurrentUserService;
import com.assetflow.shared.enums.RecordStatus;
import com.assetflow.shared.service.BaseQueryService;
import com.assetflow.shared.specification.BaseSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class AssetQueryServiceImpl extends BaseQueryService<Asset, Long, AssetRepository> implements AssetQueryService {

    private final AssetRepository assetRepository;
    private final AssetMapper assetMapper;
    private final CurrentUserService currentUserService;

    @Override
    protected AssetRepository getRepository() {
        return assetRepository;
    }

    @Override
    protected String getResourceName() {
        return "Asset";
    }

    @Override
    public Page<AssetSummaryResponse> findAllActive(Long categoryId, AssetStatus status, Long assignedTo, String search, Pageable pageable) {
        Long currentUserId = currentUserService.getCurrentUserId();
        boolean isEmployeeOnly = currentUserService.hasRole("EMPLOYEE") &&
                !currentUserService.hasRole("ASSET_MANAGER") &&
                !currentUserService.hasRole("DEPT_HEAD") &&
                !currentUserService.hasRole("ADMIN");

        Specification<Asset> spec = Specification.where(BaseSpecification.<Asset>isActive())
                .and(AssetSpecification.hasCategory(categoryId))
                .and(AssetSpecification.hasStatus(status))
                .and(AssetSpecification.isAssignedTo(assignedTo))
                .and(AssetSpecification.search(search))
                .and(AssetSpecification.visibleToUser(currentUserId, isEmployeeOnly));

        return assetRepository.findAll(spec, pageable).map(assetMapper::toSummaryResponse);
    }

    @Override
    public AssetDetailResponse findActiveById(Long id) {
        Asset asset = findActiveEntityById(id);
        
        // Employee visibility check
        boolean isEmployeeOnly = currentUserService.hasRole("EMPLOYEE") &&
                !currentUserService.hasRole("ASSET_MANAGER") &&
                !currentUserService.hasRole("DEPT_HEAD") &&
                !currentUserService.hasRole("ADMIN");
        if (isEmployeeOnly) {
            boolean isAvailable = asset.getLifecycleStatus() == AssetStatus.AVAILABLE;
            boolean isAssignedToMe = asset.getAssignedUser() != null &&
                    asset.getAssignedUser().getId().equals(currentUserService.getCurrentUserId());
            if (!isAvailable && !isAssignedToMe) {
                throw new AccessDeniedException("Not authorized to view this asset");
            }
        }
        
        return assetMapper.toDetailResponse(asset);
    }
}
