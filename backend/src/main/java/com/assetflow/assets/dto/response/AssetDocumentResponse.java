package com.assetflow.assets.dto.response;

import lombok.Data;
import java.time.Instant;

@Data
public class AssetDocumentResponse {
    private Long id;
    private Long assetId;
    private String documentType;
    private String fileUrl;
    private String fileName;
    private String status;
    private Instant createdAt;
}
