package com.assetflow.security.repository;

import com.assetflow.security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.assetflow.shared.enums.RecordStatus;
import com.assetflow.shared.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface RoleRepository extends BaseRepository<Role, Long> {

    Optional<Role> findByName(String name);

    Optional<Role> findByIdAndStatus(Long id, RecordStatus status);
}
