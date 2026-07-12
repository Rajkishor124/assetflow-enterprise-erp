package com.assetflow.assets.controller;

import com.assetflow.assets.dto.request.AssetCategoryRequest;
import com.assetflow.assets.dto.response.AssetCategoryDetailResponse;
import com.assetflow.assets.dto.response.AssetCategorySummaryResponse;
import com.assetflow.assets.service.command.AssetCategoryCommandService;
import com.assetflow.assets.service.query.AssetCategoryQueryService;
import com.assetflow.shared.dto.ApiResponse;
import com.assetflow.util.ApiPaths;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPaths.ASSET_CATEGORIES)
@RequiredArgsConstructor
public class AssetCategoryController {

    private final AssetCategoryQueryService assetCategoryQueryService;
    private final AssetCategoryCommandService assetCategoryCommandService;

    @GetMapping
    public ApiResponse<Page<AssetCategorySummaryResponse>> getAllCategories(Pageable pageable) {
        Page<AssetCategorySummaryResponse> cats = assetCategoryQueryService.findAllActive(pageable);
        return ApiResponse.success(cats, "Asset categories retrieved successfully");
    }

    @GetMapping("/{id}")
    public ApiResponse<AssetCategoryDetailResponse> getCategoryById(@PathVariable Long id) {
        AssetCategoryDetailResponse cat = assetCategoryQueryService.findActiveById(id);
        return ApiResponse.success(cat, "Category details retrieved successfully");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'ASSET_MANAGER')")
    public ApiResponse<AssetCategoryDetailResponse> createCategory(@Valid @RequestBody AssetCategoryRequest request) {
        AssetCategoryDetailResponse cat = assetCategoryCommandService.createCategory(request);
        return ApiResponse.success(cat, "Asset category created successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ASSET_MANAGER')")
    public ApiResponse<AssetCategoryDetailResponse> updateCategory(@PathVariable Long id, @Valid @RequestBody AssetCategoryRequest request) {
        AssetCategoryDetailResponse cat = assetCategoryCommandService.updateCategory(id, request);
        return ApiResponse.success(cat, "Asset category updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ASSET_MANAGER')")
    public ApiResponse<Void> deleteCategory(@PathVariable Long id) {
        assetCategoryCommandService.deleteCategory(id);
        return ApiResponse.success(null, "Asset category deleted successfully");
    }
}
