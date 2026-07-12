package com.assetflow.assets.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Instant;
import java.util.List;

@Data
public class AssetDetailResponse {
    private Long id;
    private String assetTag;
    private String name;
    private String description;
    private String serialNumber;
    private String manufacturer;
    private String model;
    private BigDecimal purchasePrice;
    private LocalDate purchaseDate;
    private LocalDate warrantyExpiry;
    private String lifecycleStatus;
    private Long categoryId;
    private String categoryName;
    private Long departmentId;
    private String departmentName;
    private Long assignedUserId;
    private String assignedUserName;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
    private List<AssetDocumentResponse> documents;
}
