package com.assetflow.assets.repository;

import com.assetflow.assets.entity.AssetDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetDocumentRepository extends JpaRepository<AssetDocument, Long> {}

