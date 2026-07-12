package com.assetflow.maintenance.service;

import com.assetflow.assets.entity.Asset;
import com.assetflow.assets.enums.AssetStatus;
import com.assetflow.assets.repository.AssetRepository;
import com.assetflow.exception.ResourceNotFoundException;
import com.assetflow.maintenance.dto.MaintenanceRecordRequest;
import com.assetflow.maintenance.dto.MaintenanceRecordResponse;
import com.assetflow.maintenance.entity.MaintenanceRequest;
import com.assetflow.maintenance.enums.MaintenanceStatus;
import com.assetflow.maintenance.mapper.MaintenanceMapper;
import com.assetflow.maintenance.repository.MaintenanceRequestRepository;
import com.assetflow.organization.entity.User;
import com.assetflow.organization.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class MaintenanceService {

    private final MaintenanceRequestRepository maintenanceRepository;
    private final AssetRepository assetRepository;
    private final UserRepository userRepository;
    private final MaintenanceMapper mapper;

    @Transactional(readOnly = true)
    public Page<MaintenanceRecordResponse> getAllMaintenanceRequests(Pageable pageable) {
        return maintenanceRepository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Transactional
    public MaintenanceRecordResponse createMaintenanceRequest(MaintenanceRecordRequest request) {
        Asset asset = assetRepository.findById(request.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));

        User reporter = userRepository.findById(request.getReportedById())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        MaintenanceRequest maintenance = new MaintenanceRequest();
        maintenance.setAsset(asset);
        maintenance.setReportedBy(reporter);
        maintenance.setTitle(request.getTitle());
        maintenance.setDescription(request.getDescription());
        maintenance.setPriority(request.getPriority());
        maintenance.setMaintenanceStatus(MaintenanceStatus.PENDING);
        maintenance.setScheduledDate(request.getScheduledDate());

        // Update asset status to UNDER_MAINTENANCE
        asset.setLifecycleStatus(AssetStatus.UNDER_MAINTENANCE);
        assetRepository.save(asset);

        return mapper.toResponse(maintenanceRepository.save(maintenance));
    }

    @Transactional
    public MaintenanceRecordResponse completeMaintenance(Long id) {
        MaintenanceRequest maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance request not found"));

        if (maintenance.getMaintenanceStatus() == MaintenanceStatus.RESOLVED || maintenance.getMaintenanceStatus() == MaintenanceStatus.CLOSED) {
            throw new IllegalArgumentException("Maintenance is already completed or closed");
        }

        maintenance.setMaintenanceStatus(MaintenanceStatus.RESOLVED);
        maintenance.setResolutionDate(Instant.now());
        
        // Put the asset back to AVAILABLE
        Asset asset = maintenance.getAsset();
        asset.setLifecycleStatus(AssetStatus.AVAILABLE);
        assetRepository.save(asset);

        return mapper.toResponse(maintenanceRepository.save(maintenance));
    }
}
