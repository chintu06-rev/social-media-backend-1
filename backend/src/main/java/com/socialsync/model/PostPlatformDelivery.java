package com.socialsync.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "post_platform_deliveries")
public class PostPlatformDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @JsonBackReference
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "social_account_id", nullable = false)
    private SocialAccount socialAccount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlatformType platform;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status = DeliveryStatus.PENDING;

    private String platformPostId;

    private String platformPostUrl;

    @Column(length = 1024)
    private String errorMessage;

    private LocalDateTime attemptedAt;

    private LocalDateTime deliveredAt;

    public PostPlatformDelivery() {
    }

    public PostPlatformDelivery(Post post, SocialAccount socialAccount, PlatformType platform) {
        this.post = post;
        this.socialAccount = socialAccount;
        this.platform = platform;
        this.status = DeliveryStatus.PENDING;
        this.attemptedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public SocialAccount getSocialAccount() {
        return socialAccount;
    }

    public void setSocialAccount(SocialAccount socialAccount) {
        this.socialAccount = socialAccount;
    }

    public PlatformType getPlatform() {
        return platform;
    }

    public void setPlatform(PlatformType platform) {
        this.platform = platform;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public String getPlatformPostId() {
        return platformPostId;
    }

    public void setPlatformPostId(String platformPostId) {
        this.platformPostId = platformPostId;
    }

    public String getPlatformPostUrl() {
        return platformPostUrl;
    }

    public void setPlatformPostUrl(String platformPostUrl) {
        this.platformPostUrl = platformPostUrl;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getAttemptedAt() {
        return attemptedAt;
    }

    public void setAttemptedAt(LocalDateTime attemptedAt) {
        this.attemptedAt = attemptedAt;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }
}
