package com.assetflow.audits.mapper;

import com.assetflow.audits.dto.AuditCycleResponse;
import com.assetflow.audits.entity.AuditCycle;
import org.springframework.stereotype.Component;

@Component
public class AuditMapper {

    public AuditCycleResponse toResponse(AuditCycle cycle) {
        if (cycle == null) {
            return null;
        }

        return AuditCycleResponse.builder()
                .id(cycle.getId())
                .name(cycle.getName())
                .description(cycle.getDescription())
                .startDate(cycle.getStartDate())
                .endDate(cycle.getEndDate())
                .auditStatus(cycle.getAuditStatus())
                .initiatedById(cycle.getInitiatedBy() != null ? cycle.getInitiatedBy().getId() : null)
                .initiatedByName(cycle.getInitiatedBy() != null ? cycle.getInitiatedBy().getFirstName() + " " + cycle.getInitiatedBy().getLastName() : null)
                .createdAt(cycle.getCreatedAt())
                .updatedAt(cycle.getUpdatedAt())
                .build();
    }
}
