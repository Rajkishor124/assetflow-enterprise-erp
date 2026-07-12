package com.assetflow.organization.repository;

import com.assetflow.organization.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.assetflow.shared.enums.RecordStatus;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {

    Optional<Department> findByIdAndStatus(Long id, RecordStatus status);

    boolean existsByNameAndParentId(String name, Long parentId);

    boolean existsByNameAndParentIsNull(String name);
}

