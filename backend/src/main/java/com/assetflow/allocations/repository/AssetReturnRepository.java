package com.assetflow.allocations.repository;

import com.assetflow.allocations.entity.AssetReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetReturnRepository extends JpaRepository<AssetReturn, Long> {}

