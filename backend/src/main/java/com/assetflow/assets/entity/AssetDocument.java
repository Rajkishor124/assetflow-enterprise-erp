package com.assetflow.assets.entity;

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
@Table(name = "asset_documents")
public class AssetDocument extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String documentType;

    @NotBlank
    @Column(nullable = false, length = 255)
    private String fileUrl;

    @NotBlank
    @Column(nullable = false, length = 255)
    private String fileName;
}
