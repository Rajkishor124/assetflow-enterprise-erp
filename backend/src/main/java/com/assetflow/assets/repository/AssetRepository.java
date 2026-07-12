package com.assetflow.assets.repository;

import com.assetflow.assets.entity.Asset;
import com.assetflow.shared.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends BaseRepository<Asset, Long> {
    boolean existsByAssetTag(String assetTag);
    boolean existsBySerialNumber(String serialNumber);
}
