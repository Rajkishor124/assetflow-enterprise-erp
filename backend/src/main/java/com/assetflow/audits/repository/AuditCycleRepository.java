package com.assetflow.audits.repository;

import com.assetflow.audits.entity.AuditCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditCycleRepository extends JpaRepository<AuditCycle, Long> {}

