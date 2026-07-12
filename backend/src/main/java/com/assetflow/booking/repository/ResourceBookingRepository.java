package com.assetflow.booking.repository;

import com.assetflow.booking.entity.ResourceBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceBookingRepository extends JpaRepository<ResourceBooking, Long> {}

