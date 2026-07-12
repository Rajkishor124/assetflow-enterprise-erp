package com.assetflow.audits.entity;

import com.assetflow.assets.entity.Asset;
import com.assetflow.audits.enums.AuditItemResult;
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
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "audit_items")
public class AuditItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "audit_cycle_id", nullable = false)
    private AuditCycle auditCycle;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AuditItemResult result = AuditItemResult.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audited_by_id")
    private User auditedBy;

    private Instant auditedAt;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
