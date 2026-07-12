package com.assetflow.activitylog.entity;

import com.assetflow.core.entity.BaseEntity;
import com.assetflow.organization.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "activity_logs")
public class ActivityLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // Nullable for system-generated events

    @NotBlank
    @Column(nullable = false, length = 50)
    private String action; // e.g. ASSET_CREATED, ALLOCATION_APPROVED

    @NotBlank
    @Column(nullable = false, length = 50)
    private String entityType; // e.g. ASSET, BOOKING

    private Long entityId;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String oldValues; // JSON snippet

    @Column(columnDefinition = "TEXT")
    private String newValues; // JSON snippet

    @Column(length = 50)
    private String ipAddress;
}
