package com.socialsync.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

public class CreatePostRequest {

    @NotBlank(message = "Caption cannot be empty")
    private String caption;

    private String mediaUrl;

    private String mediaType = "IMAGE";

    @NotEmpty(message = "At least one target social account must be selected")
    private List<Long> targetAccountIds;

    private LocalDateTime scheduledAt;

    private boolean publishImmediately = true;

    private Long userId;

    public CreatePostRequest() {
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public List<Long> getTargetAccountIds() {
        return targetAccountIds;
    }

    public void setTargetAccountIds(List<Long> targetAccountIds) {
        this.targetAccountIds = targetAccountIds;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public boolean isPublishImmediately() {
        return publishImmediately;
    }

    public void setPublishImmediately(boolean publishImmediately) {
        this.publishImmediately = publishImmediately;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
