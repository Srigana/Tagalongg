package com.tagalong.repository;

import com.tagalong.entity.RideRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RideRequestRepository extends JpaRepository<RideRequest, UUID> {
    List<RideRequest> findByPostId(UUID postId);
    List<RideRequest> findByRequesterId(UUID requesterId);
}
