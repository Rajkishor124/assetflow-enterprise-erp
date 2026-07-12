package com.assetflow.assets.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssetDocumentRequest {

    @NotNull(message = "Asset ID is required")
    private Long assetId;

    @NotBlank(message = "Document type is required")
    private String documentType;

    @NotBlank(message = "File URL is required")
    private String fileUrl;

    @NotBlank(message = "File name is required")
    private String fileName;
}
