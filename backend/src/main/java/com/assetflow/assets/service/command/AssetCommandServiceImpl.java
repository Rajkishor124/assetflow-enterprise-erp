package com.assetflow.assets.service.command;

import com.assetflow.assets.dto.request.AssetRequest;
import com.assetflow.assets.dto.response.AssetDetailResponse;
import com.assetflow.assets.entity.Asset;
import com.assetflow.assets.entity.AssetCategory;
import com.assetflow.assets.enums.AssetStatus;
import com.assetflow.assets.mapper.AssetMapper;
import com.assetflow.assets.repository.AssetRepository;
import com.assetflow.assets.service.query.AssetCategoryQueryService;
import com.assetflow.assets.service.query.AssetQueryService;
import com.assetflow.exception.BusinessRuleViolationException;
import com.assetflow.exception.DuplicateResourceException;
import com.assetflow.exception.InvalidStateException;
import com.assetflow.organization.entity.Department;
import com.assetflow.organization.entity.User;
import com.assetflow.organization.service.query.DepartmentQueryService;
import com.assetflow.organization.service.query.UserQueryService;
import com.assetflow.shared.service.BaseCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
class AssetCommandServiceImpl extends BaseCommandService<Asset, Long, AssetRepository> implements AssetCommandService {

    private final AssetRepository assetRepository;
    private final AssetMapper assetMapper;
    private final AssetQueryService assetQueryService;
    private final AssetCategoryQueryService assetCategoryQueryService;
    private final UserQueryService userQueryService;
    private final DepartmentQueryService departmentQueryService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    protected AssetRepository getRepository() {
        return assetRepository;
    }

    @Override
    protected String getResourceName() {
        return "Asset";
    }

    @Override
    public AssetDetailResponse createAsset(AssetRequest request) {
        if (assetRepository.existsByAssetTag(request.getAssetTag())) {
            throw new DuplicateResourceException("Asset", "assetTag", request.getAssetTag());
        }
        if (assetRepository.existsBySerialNumber(request.getSerialNumber())) {
            throw new DuplicateResourceException("Asset", "serialNumber", request.getSerialNumber());
        }

        Asset asset = assetMapper.toEntity(request);
        
        AssetCategory category = assetCategoryQueryService.findActiveEntityById(request.getCategoryId());
        asset.setCategory(category);
        asset.setLifecycleStatus(AssetStatus.AVAILABLE);

        Asset saved = assetRepository.save(asset);
        eventPublisher.publishEvent(new com.assetflow.assets.event.AssetCreatedEvent(saved.getId(), saved.getAssetTag()));
        return assetMapper.toDetailResponse(saved);
    }

    @Override
    public AssetDetailResponse updateAssetDetails(Long id, AssetRequest request) {
        Asset asset = assetQueryService.findActiveEntityById(id);

        if (!asset.getSerialNumber().equals(request.getSerialNumber()) && assetRepository.existsBySerialNumber(request.getSerialNumber())) {
            throw new DuplicateResourceException("Asset", "serialNumber", request.getSerialNumber());
        }

        assetMapper.updateEntityFromRequest(request, asset);

        if (!asset.getCategory().getId().equals(request.getCategoryId())) {
            AssetCategory category = assetCategoryQueryService.findActiveEntityById(request.getCategoryId());
            asset.setCategory(category);
        }

        Asset saved = assetRepository.save(asset);
        return assetMapper.toDetailResponse(saved);
    }

    @Override
    public void allocateAsset(Long id, Long userId, Long departmentId) {
        Asset asset = assetQueryService.findActiveEntityById(id);
        
        if (asset.getLifecycleStatus() != AssetStatus.AVAILABLE) {
            throw new InvalidStateException("Asset must be AVAILABLE to allocate. Current status: " + asset.getLifecycleStatus());
        }
        
        User user = null;
        if (userId != null) {
            user = userQueryService.findActiveEntityById(userId);
        }
        
        Department department = null;
        if (departmentId != null) {
            department = departmentQueryService.findActiveEntityById(departmentId);
        }
        
        if (user == null && department == null) {
            throw new BusinessRuleViolationException("Allocation must have either a user or a department assigned");
        }

        asset.setLifecycleStatus(AssetStatus.ALLOCATED);
        asset.setAssignedUser(user);
        asset.setDepartment(department);
        assetRepository.save(asset);
        eventPublisher.publishEvent(new com.assetflow.assets.event.AssetAllocatedEvent(id, userId, departmentId));
    }

    @Override
    public void returnAsset(Long id) {
        Asset asset = assetQueryService.findActiveEntityById(id);
        
        if (asset.getLifecycleStatus() != AssetStatus.ALLOCATED) {
            throw new InvalidStateException("Only ALLOCATED assets can be returned. Current status: " + asset.getLifecycleStatus());
        }

        asset.setLifecycleStatus(AssetStatus.AVAILABLE);
        asset.setAssignedUser(null);
        asset.setDepartment(null);
        assetRepository.save(asset);
        eventPublisher.publishEvent(new com.assetflow.assets.event.AssetReturnedEvent(id));
    }

    @Override
    public void retireAsset(Long id) {
        Asset asset = assetQueryService.findActiveEntityById(id);
        
        if (asset.getLifecycleStatus() == AssetStatus.RETIRED || asset.getLifecycleStatus() == AssetStatus.DISPOSED || asset.getLifecycleStatus() == AssetStatus.LOST) {
            throw new InvalidStateException("Asset is already in an end-of-life state.");
        }

        asset.setLifecycleStatus(AssetStatus.RETIRED);
        asset.setAssignedUser(null);
        asset.setDepartment(null);
        assetRepository.save(asset);
        eventPublisher.publishEvent(new com.assetflow.assets.event.AssetRetiredEvent(id));
    }

    @Override
    public void markLost(Long id) {
        Asset asset = assetQueryService.findActiveEntityById(id);
        
        asset.setLifecycleStatus(AssetStatus.LOST);
        asset.setAssignedUser(null);
        assetRepository.save(asset);
    }

    @Override
    public void sendToMaintenance(Long id) {
        Asset asset = assetQueryService.findActiveEntityById(id);
        
        if (asset.getLifecycleStatus() != AssetStatus.AVAILABLE && asset.getLifecycleStatus() != AssetStatus.ALLOCATED) {
            throw new InvalidStateException("Only AVAILABLE or ALLOCATED assets can be sent to maintenance.");
        }

        asset.setLifecycleStatus(AssetStatus.UNDER_MAINTENANCE);
        assetRepository.save(asset);
        eventPublisher.publishEvent(new com.assetflow.assets.event.AssetMaintenanceRequestedEvent(id));
    }

    @Override
    public void restoreFromMaintenance(Long id) {
        Asset asset = assetQueryService.findActiveEntityById(id);
        
        if (asset.getLifecycleStatus() != AssetStatus.UNDER_MAINTENANCE) {
            throw new InvalidStateException("Only assets UNDER_MAINTENANCE can be restored.");
        }

        if (asset.getAssignedUser() != null || asset.getDepartment() != null) {
            asset.setLifecycleStatus(AssetStatus.ALLOCATED);
        } else {
            asset.setLifecycleStatus(AssetStatus.AVAILABLE);
        }
        
        assetRepository.save(asset);
    }
}
