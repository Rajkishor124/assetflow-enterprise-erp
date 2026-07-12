package com.assetflow.allocations.controller;

import com.assetflow.allocations.dto.AssetAllocationRequest;
import com.assetflow.allocations.dto.AssetAllocationResponse;
import com.assetflow.allocations.service.AssetAllocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/allocations")
@RequiredArgsConstructor
@Tag(name = "Asset Allocations", description = "Endpoints for managing asset allocations and returns")
public class AssetAllocationController {

    private final AssetAllocationService assetAllocationService;

    @GetMapping
    @Operation(summary = "Get all allocations", description = "Retrieves a paginated list of all asset allocations")
    @PreAuthorize("hasAnyRole('ADMIN', 'ASSET_MANAGER', 'DEPT_HEAD')")
    public ResponseEntity<Page<AssetAllocationResponse>> getAllAllocations(Pageable pageable) {
        return ResponseEntity.ok(assetAllocationService.getAllAllocations(pageable));
    }

    @PostMapping
    @Operation(summary = "Allocate an asset", description = "Allocates an asset to a user or department")
    @PreAuthorize("hasAnyRole('ADMIN', 'ASSET_MANAGER')")
    public ResponseEntity<AssetAllocationResponse> allocateAsset(@Valid @RequestBody AssetAllocationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(assetAllocationService.allocateAsset(request));
    }

    @PostMapping("/{id}/return")
    @Operation(summary = "Return an asset", description = "Marks an active allocation as returned")
    @PreAuthorize("hasAnyRole('ADMIN', 'ASSET_MANAGER', 'DEPT_HEAD')")
    public ResponseEntity<AssetAllocationResponse> returnAsset(
            @PathVariable Long id,
            @RequestParam(required = false) String notes) {
        return ResponseEntity.ok(assetAllocationService.returnAsset(id, notes));
    }
}
