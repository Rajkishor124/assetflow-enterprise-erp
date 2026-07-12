package com.assetflow.booking.repository;

import com.assetflow.booking.entity.BookableResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookableResourceRepository extends JpaRepository<BookableResource, Long> {}

