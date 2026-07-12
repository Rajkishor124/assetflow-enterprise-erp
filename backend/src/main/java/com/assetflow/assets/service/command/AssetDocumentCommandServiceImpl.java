package com.assetflow.assets.service.command;

import com.assetflow.assets.dto.request.AssetDocumentRequest;
import com.assetflow.assets.dto.response.AssetDocumentResponse;
import com.assetflow.assets.entity.AssetDocument;
import com.assetflow.assets.entity.Asset;
import com.assetflow.assets.mapper.AssetDocumentMapper;
import com.assetflow.assets.repository.AssetDocumentRepository;
import com.assetflow.assets.service.query.AssetDocumentQueryService;
import com.assetflow.assets.service.query.AssetQueryService;
import com.assetflow.shared.enums.RecordStatus;
import com.assetflow.shared.service.BaseCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
class AssetDocumentCommandServiceImpl extends BaseCommandService<AssetDocument, Long, AssetDocumentRepository> implements AssetDocumentCommandService {

    private final AssetDocumentRepository assetDocumentRepository;
    private final AssetDocumentMapper assetDocumentMapper;
    private final AssetDocumentQueryService assetDocumentQueryService;
    private final AssetQueryService assetQueryService;

    @Override
    protected AssetDocumentRepository getRepository() {
        return assetDocumentRepository;
    }

    @Override
    protected String getResourceName() {
        return "AssetDocument";
    }

    @Override
    public AssetDocumentResponse addDocument(AssetDocumentRequest request) {
        Asset asset = assetQueryService.findActiveEntityById(request.getAssetId());

        AssetDocument document = assetDocumentMapper.toEntity(request);
        document.setAsset(asset);

        AssetDocument saved = assetDocumentRepository.save(document);
        return assetDocumentMapper.toResponse(saved);
    }

    @Override
    public void removeDocument(Long id) {
        AssetDocument document = assetDocumentQueryService.findActiveEntityById(id);
        document.setStatus(RecordStatus.INACTIVE);
        assetDocumentRepository.save(document);
    }
}
