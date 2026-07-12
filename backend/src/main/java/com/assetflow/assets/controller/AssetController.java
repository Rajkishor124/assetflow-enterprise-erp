package com.assetflow.assets.controller;

import com.assetflow.assets.dto.request.AssetRequest;
import com.assetflow.assets.dto.response.AssetDetailResponse;
import com.assetflow.assets.dto.response.AssetSummaryResponse;
import com.assetflow.assets.enums.AssetStatus;
import com.assetflow.assets.service.command.AssetCommandService;
import com.assetflow.assets.service.query.AssetQueryService;
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
@RequestMapping(ApiPaths.ASSETS)
@RequiredArgsConstructor
public class AssetController {

    private final AssetQueryService assetQueryService;
    private final AssetCommandService assetCommandService;

    @GetMapping
    public ApiResponse<Page<AssetSummaryResponse>> getAllAssets(
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) AssetStatus status,
            @RequestParam(required = false) Long assignedTo,
            @RequestParam(required = false) String q,
            Pageable pageable) {
        Page<AssetSummaryResponse> assets = assetQueryService.findAllActive(category, status, assignedTo, q, pageable);
        return ApiResponse.success(assets, "Assets retrieved successfully");
    }

    @GetMapping("/{id}")
    public ApiResponse<AssetDetailResponse> getAssetById(@PathVariable Long id) {
        AssetDetailResponse asset = assetQueryService.findActiveById(id);
        return ApiResponse.success(asset, "Asset details retrieved successfully");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'ASSET_MANAGER')")
    public ApiResponse<AssetDetailResponse> createAsset(@Valid @RequestBody AssetRequest request) {
        AssetDetailResponse asset = assetCommandService.createAsset(request);
        return ApiResponse.success(asset, "Asset created successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ASSET_MANAGER')")
    public ApiResponse<AssetDetailResponse> updateAsset(@PathVariable Long id, @Valid @RequestBody AssetRequest request) {
        AssetDetailResponse asset = assetCommandService.updateAssetDetails(id, request);
        return ApiResponse.success(asset, "Asset updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ASSET_MANAGER')")
    public ApiResponse<Void> deleteAsset(@PathVariable Long id) {
        assetCommandService.retireAsset(id);
        return ApiResponse.success(null, "Asset retired successfully");
    }
}
