package com.socialsync.dto;

import com.socialsync.model.DeliveryStatus;
import com.socialsync.model.PlatformType;
import java.time.LocalDateTime;

public class DeliveryResultDto {
    private Long deliveryId;
    private Long accountId;
    private PlatformType platform;
    private String accountName;
    private String handle;
    private String avatarUrl;
    private DeliveryStatus status;
    private String platformPostId;
    private String platformPostUrl;
    private String errorMessage;
    private LocalDateTime deliveredAt;

    public DeliveryResultDto() {
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public PlatformType getPlatform() {
        return platform;
    }

    public void setPlatform(PlatformType platform) {
        this.platform = platform;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
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

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }
}
