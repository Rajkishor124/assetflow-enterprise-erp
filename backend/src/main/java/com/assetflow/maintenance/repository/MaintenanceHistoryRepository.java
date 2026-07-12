package com.assetflow.maintenance.repository;

import com.assetflow.maintenance.entity.MaintenanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceHistoryRepository extends JpaRepository<MaintenanceHistory, Long> {}

