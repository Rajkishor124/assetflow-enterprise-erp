package com.assetflow.assets.dto.response;

import lombok.Data;
import java.time.Instant;

@Data
public class AssetCategorySummaryResponse {
    private Long id;
    private String name;
    private Long parentCategoryId;
    private String parentCategoryName;
    private String status;
    private Instant createdAt;
}
