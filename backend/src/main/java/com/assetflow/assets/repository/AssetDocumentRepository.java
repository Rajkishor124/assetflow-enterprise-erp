package com.assetflow.assets.repository;

import com.assetflow.assets.entity.AssetDocument;
import com.assetflow.shared.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetDocumentRepository extends BaseRepository<AssetDocument, Long> {
    List<AssetDocument> findByAssetId(Long assetId);
}
