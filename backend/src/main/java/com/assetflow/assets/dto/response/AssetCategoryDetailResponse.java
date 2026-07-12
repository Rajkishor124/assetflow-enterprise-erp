package com.assetflow.assets.dto.response;

import lombok.Data;
import java.time.Instant;

@Data
public class AssetCategoryDetailResponse {
    private Long id;
    private String name;
    private String description;
    private Long parentCategoryId;
    private String parentCategoryName;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
}
