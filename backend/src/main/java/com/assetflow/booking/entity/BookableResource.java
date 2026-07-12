package com.assetflow.booking.entity;

import com.assetflow.assets.entity.AssetCategory;
import com.assetflow.core.entity.BaseEntity;
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
@Table(name = "bookable_resources")
public class BookableResource extends BaseEntity {

    @NotBlank
    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 50)
    private String resourceType; // e.g. MEETING_ROOM, VEHICLE, PROJECTOR

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private AssetCategory category;
}
