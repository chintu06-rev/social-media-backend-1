package com.socialsync.service;

import com.socialsync.dto.CreatePostRequest;
import com.socialsync.dto.DeliveryResultDto;
import com.socialsync.dto.PostResponse;
import com.socialsync.model.*;
import com.socialsync.repository.PostPlatformDeliveryRepository;
import com.socialsync.repository.PostRepository;
import com.socialsync.repository.SocialAccountRepository;
import com.socialsync.service.publisher.PublishResult;
import com.socialsync.service.publisher.PublisherFactory;
import com.socialsync.service.publisher.SocialPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final SocialAccountRepository accountRepository;
    private final PostPlatformDeliveryRepository deliveryRepository;
    private final PublisherFactory publisherFactory;

    public PostService(
            PostRepository postRepository,
            SocialAccountRepository accountRepository,
            PostPlatformDeliveryRepository deliveryRepository,
            PublisherFactory publisherFactory) {
        this.postRepository = postRepository;
        this.accountRepository = accountRepository;
        this.deliveryRepository = deliveryRepository;
        this.publisherFactory = publisherFactory;
    }

    @Transactional
    public PostResponse createAndPublishPost(CreatePostRequest request) {
        Post post = new Post();
        post.setCaption(request.getCaption());
        post.setMediaUrl(request.getMediaUrl());
        post.setMediaType(request.getMediaType() != null ? request.getMediaType() : "IMAGE");
        post.setScheduledAt(request.getScheduledAt());
        post.setUserId(request.getUserId());

        boolean isScheduled = !request.isPublishImmediately() && request.getScheduledAt() != null && request.getScheduledAt().isAfter(LocalDateTime.now());
        post.setStatus(isScheduled ? PostStatus.SCHEDULED : PostStatus.PUBLISHING);

        List<SocialAccount> selectedAccounts = accountRepository.findAllById(request.getTargetAccountIds());
        if (selectedAccounts.isEmpty()) {
            throw new IllegalArgumentException("No valid accounts selected for publishing.");
        }

        for (SocialAccount account : selectedAccounts) {
            PostPlatformDelivery delivery = new PostPlatformDelivery(post, account, account.getPlatform());
            post.addDelivery(delivery);
        }

        Post savedPost = postRepository.save(post);

        // If not scheduled, publish immediately across all selected platforms
        if (!isScheduled) {
            dispatchToPlatforms(savedPost);
        }

        return toResponse(savedPost);
    }

    @Transactional
    public void dispatchToPlatforms(Post post) {
        int successCount = 0;
        int failureCount = 0;

        for (PostPlatformDelivery delivery : post.getDeliveries()) {
            SocialAccount account = delivery.getSocialAccount();
            SocialPublisher publisher = publisherFactory.getPublisher(delivery.getPlatform());

            delivery.setAttemptedAt(LocalDateTime.now());
            try {
                PublishResult result = publisher.publish(post, account);
                if (result.isSuccess()) {
                    delivery.setStatus(DeliveryStatus.SUCCESS);
                    delivery.setPlatformPostId(result.getPlatformPostId());
                    delivery.setPlatformPostUrl(result.getPlatformPostUrl());
                    delivery.setDeliveredAt(LocalDateTime.now());
                    delivery.setErrorMessage(null);
                    successCount++;
                } else {
                    delivery.setStatus(DeliveryStatus.FAILED);
                    delivery.setErrorMessage(result.getErrorMessage());
                    failureCount++;
                }
            } catch (Exception e) {
                delivery.setStatus(DeliveryStatus.FAILED);
                delivery.setErrorMessage("Unexpected publishing error: " + e.getMessage());
                failureCount++;
            }
        }

        if (failureCount == 0 && successCount > 0) {
            post.setStatus(PostStatus.PUBLISHED);
            post.setPublishedAt(LocalDateTime.now());
        } else if (successCount > 0 && failureCount > 0) {
            post.setStatus(PostStatus.PARTIALLY_PUBLISHED);
            post.setPublishedAt(LocalDateTime.now());
        } else {
            post.setStatus(PostStatus.FAILED);
        }

        post.setUpdatedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    @Transactional
    public PostResponse retryDelivery(Long deliveryId) {
        PostPlatformDelivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found with id: " + deliveryId));

        Post post = delivery.getPost();
        SocialAccount account = delivery.getSocialAccount();
        SocialPublisher publisher = publisherFactory.getPublisher(delivery.getPlatform());

        delivery.setAttemptedAt(LocalDateTime.now());
        try {
            PublishResult result = publisher.publish(post, account);
            if (result.isSuccess()) {
                delivery.setStatus(DeliveryStatus.SUCCESS);
                delivery.setPlatformPostId(result.getPlatformPostId());
                delivery.setPlatformPostUrl(result.getPlatformPostUrl());
                delivery.setDeliveredAt(LocalDateTime.now());
                delivery.setErrorMessage(null);
            } else {
                delivery.setStatus(DeliveryStatus.FAILED);
                delivery.setErrorMessage(result.getErrorMessage());
            }
        } catch (Exception e) {
            delivery.setStatus(DeliveryStatus.FAILED);
            delivery.setErrorMessage("Retry failed: " + e.getMessage());
        }

        deliveryRepository.save(delivery);

        // Re-evaluate post status
        boolean allSuccess = post.getDeliveries().stream().allMatch(d -> d.getStatus() == DeliveryStatus.SUCCESS);
        boolean anySuccess = post.getDeliveries().stream().anyMatch(d -> d.getStatus() == DeliveryStatus.SUCCESS);

        if (allSuccess) {
            post.setStatus(PostStatus.PUBLISHED);
        } else if (anySuccess) {
            post.setStatus(PostStatus.PARTIALLY_PUBLISHED);
        } else {
            post.setStatus(PostStatus.FAILED);
        }
        post.setUpdatedAt(LocalDateTime.now());
        postRepository.save(post);

        return toResponse(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts(Long userId) {
        List<Post> list = (userId != null)
                ? postRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                : postRepository.findAllByOrderByCreatedAtDesc();

        return list.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id) {
        return postRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));
    }

    public PostResponse toResponse(Post post) {
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setCaption(post.getCaption());
        response.setMediaUrl(post.getMediaUrl());
        response.setMediaType(post.getMediaType());
        response.setStatus(post.getStatus());
        response.setScheduledAt(post.getScheduledAt());
        response.setPublishedAt(post.getPublishedAt());
        response.setCreatedAt(post.getCreatedAt());

        List<DeliveryResultDto> deliveryDtos = new ArrayList<>();
        if (post.getDeliveries() != null) {
            for (PostPlatformDelivery delivery : post.getDeliveries()) {
                DeliveryResultDto dto = new DeliveryResultDto();
                dto.setDeliveryId(delivery.getId());
                dto.setPlatform(delivery.getPlatform());
                dto.setStatus(delivery.getStatus());
                dto.setPlatformPostId(delivery.getPlatformPostId());
                dto.setPlatformPostUrl(delivery.getPlatformPostUrl());
                dto.setErrorMessage(delivery.getErrorMessage());
                dto.setDeliveredAt(delivery.getDeliveredAt());

                if (delivery.getSocialAccount() != null) {
                    dto.setAccountId(delivery.getSocialAccount().getId());
                    dto.setAccountName(delivery.getSocialAccount().getAccountName());
                    dto.setHandle(delivery.getSocialAccount().getHandle());
                    dto.setAvatarUrl(delivery.getSocialAccount().getAvatarUrl());
                }
                deliveryDtos.add(dto);
            }
        }
        response.setDeliveries(deliveryDtos);
        return response;
    }
}
