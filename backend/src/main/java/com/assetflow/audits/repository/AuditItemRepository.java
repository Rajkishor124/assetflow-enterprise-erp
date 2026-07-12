package com.assetflow.audits.repository;

import com.assetflow.audits.entity.AuditItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditItemRepository extends JpaRepository<AuditItem, Long> {}

