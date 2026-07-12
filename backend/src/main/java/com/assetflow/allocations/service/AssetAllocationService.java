package com.assetflow.allocations.service;

import com.assetflow.allocations.dto.AssetAllocationRequest;
import com.assetflow.allocations.dto.AssetAllocationResponse;
import com.assetflow.allocations.entity.AssetAllocation;
import com.assetflow.allocations.enums.AllocationStatus;
import com.assetflow.allocations.mapper.AssetAllocationMapper;
import com.assetflow.allocations.repository.AssetAllocationRepository;
import com.assetflow.assets.entity.Asset;
import com.assetflow.assets.enums.AssetStatus;
import com.assetflow.assets.repository.AssetRepository;
import com.assetflow.exception.ResourceNotFoundException;
import com.assetflow.organization.entity.Department;
import com.assetflow.organization.entity.User;
import com.assetflow.organization.repository.DepartmentRepository;
import com.assetflow.organization.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AssetAllocationService {

    private final AssetAllocationRepository assetAllocationRepository;
    private final AssetRepository assetRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final AssetAllocationMapper assetAllocationMapper;

    @Transactional(readOnly = true)
    public Page<AssetAllocationResponse> getAllAllocations(Pageable pageable) {
        return assetAllocationRepository.findAll(pageable)
                .map(assetAllocationMapper::toResponse);
    }

    @Transactional
    public AssetAllocationResponse allocateAsset(AssetAllocationRequest request) {
        Asset asset = assetRepository.findById(request.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id " + request.getAssetId()));

        if (asset.getLifecycleStatus() != AssetStatus.AVAILABLE) {
            throw new IllegalArgumentException("Asset is not available for allocation. Current status: " + asset.getLifecycleStatus());
        }

        User user = null;
        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + request.getUserId()));
        }

        Department department = null;
        if (request.getDepartmentId() != null) {
            department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + request.getDepartmentId()));
        }

        if (user == null && department == null) {
            throw new IllegalArgumentException("Allocation must have either a user or a department assigned.");
        }

        AssetAllocation allocation = new AssetAllocation();
        allocation.setAsset(asset);
        allocation.setUser(user);
        allocation.setDepartment(department);
        allocation.setAllocationDate(request.getAllocationDate());
        allocation.setExpectedReturnDate(request.getExpectedReturnDate());
        allocation.setAllocationStatus(AllocationStatus.ACTIVE);
        allocation.setNotes(request.getNotes());

        asset.setLifecycleStatus(AssetStatus.ALLOCATED);
        assetRepository.save(asset);

        AssetAllocation savedAllocation = assetAllocationRepository.save(allocation);
        return assetAllocationMapper.toResponse(savedAllocation);
    }

    @Transactional
    public AssetAllocationResponse returnAsset(Long id, String notes) {
        AssetAllocation allocation = assetAllocationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset allocation not found with id " + id));

        if (allocation.getAllocationStatus() != AllocationStatus.ACTIVE) {
            throw new IllegalArgumentException("Allocation is already returned or cancelled.");
        }

        allocation.setActualReturnDate(Instant.now());
        allocation.setAllocationStatus(AllocationStatus.RETURNED);
        
        if (notes != null && !notes.isEmpty()) {
            allocation.setNotes(allocation.getNotes() + "\nReturn notes: " + notes);
        }

        Asset asset = allocation.getAsset();
        asset.setLifecycleStatus(AssetStatus.AVAILABLE);
        assetRepository.save(asset);

        AssetAllocation savedAllocation = assetAllocationRepository.save(allocation);
        return assetAllocationMapper.toResponse(savedAllocation);
    }
}
