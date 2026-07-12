package com.assetflow.maintenance.mapper;

import com.assetflow.maintenance.dto.MaintenanceRecordResponse;
import com.assetflow.maintenance.entity.MaintenanceRequest;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceMapper {

    public MaintenanceRecordResponse toResponse(MaintenanceRequest request) {
        if (request == null) {
            return null;
        }

        return MaintenanceRecordResponse.builder()
                .id(request.getId())
                .assetId(request.getAsset() != null ? request.getAsset().getId() : null)
                .assetName(request.getAsset() != null ? request.getAsset().getName() : null)
                .assetTag(request.getAsset() != null ? request.getAsset().getAssetTag() : null)
                .reportedById(request.getReportedBy() != null ? request.getReportedBy().getId() : null)
                .reportedByName(request.getReportedBy() != null ? request.getReportedBy().getFirstName() + " " + request.getReportedBy().getLastName() : null)
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .maintenanceStatus(request.getMaintenanceStatus())
                .scheduledDate(request.getScheduledDate())
                .resolutionDate(request.getResolutionDate())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .build();
    }
}
