package com.socialsync.service;

import com.socialsync.dto.DashboardStatsDto;
import com.socialsync.model.DeliveryStatus;
import com.socialsync.model.PlatformType;
import com.socialsync.model.PostStatus;
import com.socialsync.repository.PostPlatformDeliveryRepository;
import com.socialsync.repository.PostRepository;
import com.socialsync.repository.SocialAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final PostRepository postRepository;
    private final SocialAccountRepository accountRepository;
    private final PostPlatformDeliveryRepository deliveryRepository;

    public DashboardService(
            PostRepository postRepository,
            SocialAccountRepository accountRepository,
            PostPlatformDeliveryRepository deliveryRepository) {
        this.postRepository = postRepository;
        this.accountRepository = accountRepository;
        this.deliveryRepository = deliveryRepository;
    }

    @Transactional(readOnly = true)
    public DashboardStatsDto getStats(Long userId) {
        long totalPosts;
        long published;
        long scheduled;
        long failed;
        long connectedAccounts;

        if (userId != null) {
            totalPosts = postRepository.countByUserId(userId);
            published = postRepository.countByUserIdAndStatus(userId, PostStatus.PUBLISHED);
            scheduled = postRepository.countByUserIdAndStatus(userId, PostStatus.SCHEDULED);
            failed = postRepository.countByUserIdAndStatus(userId, PostStatus.FAILED);
            connectedAccounts = accountRepository.findByUserIdAndActiveTrue(userId).size();
        } else {
            totalPosts = postRepository.count();
            published = postRepository.countByStatus(PostStatus.PUBLISHED);
            scheduled = postRepository.countByStatus(PostStatus.SCHEDULED);
            failed = postRepository.countByStatus(PostStatus.FAILED);
            connectedAccounts = accountRepository.findByActiveTrue().size();
        }

        long successfulDeliveries = deliveryRepository.countByStatus(DeliveryStatus.SUCCESS);
        long totalDeliveries = deliveryRepository.count();

        double successRate = totalDeliveries > 0
                ? Math.round(((double) successfulDeliveries / totalDeliveries) * 1000.0) / 10.0
                : 100.0;

        Map<String, Long> byPlatform = new HashMap<>();
        for (PlatformType type : PlatformType.values()) {
            byPlatform.put(type.name(), deliveryRepository.countByPlatformAndStatus(type, DeliveryStatus.SUCCESS));
        }

        DashboardStatsDto stats = new DashboardStatsDto();
        stats.setTotalPosts(totalPosts);
        stats.setPublishedPosts(published);
        stats.setScheduledPosts(scheduled);
        stats.setFailedPosts(failed);
        stats.setConnectedAccounts(connectedAccounts);
        stats.setPostsByPlatform(byPlatform);
        stats.setSuccessRate(successRate);

        return stats;
    }
}
