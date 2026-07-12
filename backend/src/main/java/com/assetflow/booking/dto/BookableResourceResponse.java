package com.assetflow.booking.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookableResourceResponse {
    private Long id;
    private String name;
    private String resourceType;
    private String description;
    private Long categoryId;
    private String categoryName;
}
