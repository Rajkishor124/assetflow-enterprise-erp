package com.assetflow.allocations.entity;

import com.assetflow.assets.entity.Asset;
import com.assetflow.core.entity.BaseEntity;
import com.assetflow.organization.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "asset_returns")
public class AssetReturn extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "returned_by_id", nullable = false)
    private User returnedBy;

    @Column(nullable = false)
    private Instant returnDate;

    @Column(length = 255)
    private String condition;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
