package com.assetflow.assets.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AssetSummaryResponse {
    private Long id;
    private String assetTag;
    private String name;
    private String serialNumber;
    private String manufacturer;
    private String model;
    private String lifecycleStatus;
    private Long categoryId;
    private String categoryName;
    private Long departmentId;
    private String departmentName;
    private Long assignedUserId;
    private String assignedUserName;
}
