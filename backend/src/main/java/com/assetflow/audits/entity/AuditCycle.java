package com.assetflow.audits.entity;

import com.assetflow.audits.enums.AuditStatus;
import com.assetflow.core.entity.BaseEntity;
import com.assetflow.organization.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "audit_cycles")
public class AuditCycle extends BaseEntity {

    @NotBlank
    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Instant startDate;

    private Instant endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AuditStatus auditStatus = AuditStatus.OPEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiated_by_id")
    private User initiatedBy;
}
