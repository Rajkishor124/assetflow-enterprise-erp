package com.assetflow.organization.repository;

import com.assetflow.organization.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.assetflow.shared.enums.RecordStatus;
import com.assetflow.shared.repository.BaseRepository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends BaseRepository<Department, Long> {

    Optional<Department> findByIdAndStatus(Long id, RecordStatus status);

    boolean existsByNameAndParentId(String name, Long parentId);

    boolean existsByNameAndParentIsNull(String name);
}

