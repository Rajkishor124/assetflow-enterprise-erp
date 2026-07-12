package com.assetflow.allocations.entity;

import com.assetflow.allocations.enums.AllocationStatus;
import com.assetflow.assets.entity.Asset;
import com.assetflow.core.entity.BaseEntity;
import com.assetflow.organization.entity.Department;
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
@Table(name = "asset_allocations")
public class AssetAllocation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(nullable = false)
    private Instant allocationDate;

    private Instant expectedReturnDate;

    private Instant actualReturnDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AllocationStatus allocationStatus = AllocationStatus.ACTIVE;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
