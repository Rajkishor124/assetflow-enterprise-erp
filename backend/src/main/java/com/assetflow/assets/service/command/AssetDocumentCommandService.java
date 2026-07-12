package com.assetflow.assets.service.command;

import com.assetflow.assets.dto.request.AssetDocumentRequest;
import com.assetflow.assets.dto.response.AssetDocumentResponse;

public interface AssetDocumentCommandService {
    AssetDocumentResponse addDocument(AssetDocumentRequest request);
    void removeDocument(Long id);
}
