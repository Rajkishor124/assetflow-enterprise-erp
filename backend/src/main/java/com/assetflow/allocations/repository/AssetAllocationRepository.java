package com.assetflow.allocations.repository;

import com.assetflow.allocations.entity.AssetAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetAllocationRepository extends JpaRepository<AssetAllocation, Long> {}

