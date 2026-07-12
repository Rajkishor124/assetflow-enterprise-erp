package com.assetflow.assets.service.command;

import com.assetflow.assets.dto.request.AssetCategoryRequest;
import com.assetflow.assets.dto.response.AssetCategoryDetailResponse;
import com.assetflow.assets.entity.AssetCategory;
import com.assetflow.assets.mapper.AssetCategoryMapper;
import com.assetflow.assets.repository.AssetCategoryRepository;
import com.assetflow.assets.service.query.AssetCategoryQueryService;
import com.assetflow.exception.DuplicateResourceException;
import com.assetflow.shared.enums.RecordStatus;
import com.assetflow.shared.service.BaseCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
class AssetCategoryCommandServiceImpl extends BaseCommandService<AssetCategory, Long, AssetCategoryRepository> implements AssetCategoryCommandService {

    private final AssetCategoryRepository assetCategoryRepository;
    private final AssetCategoryMapper assetCategoryMapper;
    private final AssetCategoryQueryService assetCategoryQueryService;

    @Override
    protected AssetCategoryRepository getRepository() {
        return assetCategoryRepository;
    }

    @Override
    protected String getResourceName() {
        return "AssetCategory";
    }

    @Override
    public AssetCategoryDetailResponse createCategory(AssetCategoryRequest request) {
        if (request.getParentCategoryId() != null) {
            if (assetCategoryRepository.existsByNameAndParentCategoryId(request.getName(), request.getParentCategoryId())) {
                throw new DuplicateResourceException("AssetCategory", "name and parentCategoryId", request.getName());
            }
        } else {
            if (assetCategoryRepository.existsByNameAndParentCategoryIsNull(request.getName())) {
                throw new DuplicateResourceException("AssetCategory", "name", request.getName());
            }
        }

        AssetCategory category = assetCategoryMapper.toEntity(request);

        if (request.getParentCategoryId() != null) {
            AssetCategory parent = assetCategoryQueryService.findActiveEntityById(request.getParentCategoryId());
            category.setParentCategory(parent);
        }

        AssetCategory saved = assetCategoryRepository.save(category);
        return assetCategoryMapper.toDetailResponse(saved);
    }

    @Override
    public AssetCategoryDetailResponse updateCategory(Long id, AssetCategoryRequest request) {
        AssetCategory category = assetCategoryQueryService.findActiveEntityById(id);

        boolean nameChanged = !category.getName().equals(request.getName());
        boolean parentChanged = (category.getParentCategory() == null && request.getParentCategoryId() != null) ||
                                (category.getParentCategory() != null && !category.getParentCategory().getId().equals(request.getParentCategoryId()));

        if (nameChanged || parentChanged) {
            if (request.getParentCategoryId() != null) {
                if (assetCategoryRepository.existsByNameAndParentCategoryId(request.getName(), request.getParentCategoryId())) {
                    throw new DuplicateResourceException("AssetCategory", "name and parentCategoryId", request.getName());
                }
            } else {
                if (assetCategoryRepository.existsByNameAndParentCategoryIsNull(request.getName())) {
                    throw new DuplicateResourceException("AssetCategory", "name", request.getName());
                }
            }
        }

        assetCategoryMapper.updateEntityFromRequest(request, category);

        if (request.getParentCategoryId() != null) {
            AssetCategory parent = assetCategoryQueryService.findActiveEntityById(request.getParentCategoryId());
            category.setParentCategory(parent);
        } else {
            category.setParentCategory(null);
        }

        AssetCategory saved = assetCategoryRepository.save(category);
        return assetCategoryMapper.toDetailResponse(saved);
    }

    @Override
    public void deleteCategory(Long id) {
        AssetCategory category = assetCategoryQueryService.findActiveEntityById(id);
        category.setStatus(RecordStatus.INACTIVE);
        assetCategoryRepository.save(category);
    }
}
