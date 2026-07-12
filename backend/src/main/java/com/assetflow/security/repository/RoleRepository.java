package com.assetflow.security.repository;

import com.assetflow.security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.assetflow.shared.enums.RecordStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    Optional<Role> findByName(String name);

    Optional<Role> findByIdAndStatus(Long id, RecordStatus status);
}
