package com.socialsync.service.publisher;

public class PublishResult {
    private final boolean success;
    private final String platformPostId;
    private final String platformPostUrl;
    private final String errorMessage;

    public PublishResult(boolean success, String platformPostId, String platformPostUrl, String errorMessage) {
        this.success = success;
        this.platformPostId = platformPostId;
        this.platformPostUrl = platformPostUrl;
        this.errorMessage = errorMessage;
    }

    public static PublishResult success(String platformPostId, String platformPostUrl) {
        return new PublishResult(true, platformPostId, platformPostUrl, null);
    }

    public static PublishResult failure(String errorMessage) {
        return new PublishResult(false, null, null, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getPlatformPostId() {
        return platformPostId;
    }

    public String getPlatformPostUrl() {
        return platformPostUrl;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
