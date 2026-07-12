package com.assetflow.allocations.repository;

import com.assetflow.allocations.entity.AssetTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetTransferRepository extends JpaRepository<AssetTransfer, Long> {}

