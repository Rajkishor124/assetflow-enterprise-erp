package com.assetflow.maintenance.entity;

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

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "maintenance_history")
public class MaintenanceHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "request_id", nullable = false)
    private MaintenanceRequest request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by_id")
    private User performedBy;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String actionTaken;

    @Column(precision = 10, scale = 2)
    private BigDecimal cost;

    @Column(nullable = false)
    private Instant actionDate;
}
