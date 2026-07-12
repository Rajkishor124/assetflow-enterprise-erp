package com.assetflow.assets.service.query;

import com.assetflow.assets.dto.response.AssetDocumentResponse;
import com.assetflow.assets.entity.AssetDocument;
import com.assetflow.assets.mapper.AssetDocumentMapper;
import com.assetflow.assets.repository.AssetDocumentRepository;
import com.assetflow.shared.service.BaseQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class AssetDocumentQueryServiceImpl extends BaseQueryService<AssetDocument, Long, AssetDocumentRepository> implements AssetDocumentQueryService {

    private final AssetDocumentRepository assetDocumentRepository;
    private final AssetDocumentMapper assetDocumentMapper;

    @Override
    protected AssetDocumentRepository getRepository() {
        return assetDocumentRepository;
    }

    @Override
    protected String getResourceName() {
        return "AssetDocument";
    }

    @Override
    public List<AssetDocumentResponse> findByAssetId(Long assetId) {
        return assetDocumentRepository.findByAssetId(assetId)
                .stream()
                .map(assetDocumentMapper::toResponse)
                .collect(Collectors.toList());
    }
}
