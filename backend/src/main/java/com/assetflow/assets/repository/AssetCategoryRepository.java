package com.assetflow.assets.repository;

import com.assetflow.assets.entity.AssetCategory;
import com.assetflow.shared.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetCategoryRepository extends BaseRepository<AssetCategory, Long> {
    boolean existsByName(String name);
    boolean existsByNameAndParentCategoryId(String name, Long parentCategoryId);
    boolean existsByNameAndParentCategoryIsNull(String name);
}
