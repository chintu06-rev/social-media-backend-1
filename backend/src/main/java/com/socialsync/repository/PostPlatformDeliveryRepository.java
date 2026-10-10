package com.socialsync.repository;

import com.socialsync.model.DeliveryStatus;
import com.socialsync.model.PlatformType;
import com.socialsync.model.PostPlatformDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostPlatformDeliveryRepository extends JpaRepository<PostPlatformDelivery, Long> {
    long countByPlatformAndStatus(PlatformType platform, DeliveryStatus status);
    long countByStatus(DeliveryStatus status);
}
