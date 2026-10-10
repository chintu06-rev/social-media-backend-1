package com.socialsync.dto;

import java.util.Map;

public class DashboardStatsDto {
    private long totalPosts;
    private long publishedPosts;
    private long scheduledPosts;
    private long failedPosts;
    private long connectedAccounts;
    private Map<String, Long> postsByPlatform;
    private double successRate;

    public DashboardStatsDto() {
    }

    public long getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(long totalPosts) {
        this.totalPosts = totalPosts;
    }

    public long getPublishedPosts() {
        return publishedPosts;
    }

    public void setPublishedPosts(long publishedPosts) {
        this.publishedPosts = publishedPosts;
    }

    public long getScheduledPosts() {
        return scheduledPosts;
    }

    public void setScheduledPosts(long scheduledPosts) {
        this.scheduledPosts = scheduledPosts;
    }

    public long getFailedPosts() {
        return failedPosts;
    }

    public void setFailedPosts(long failedPosts) {
        this.failedPosts = failedPosts;
    }

    public long getConnectedAccounts() {
        return connectedAccounts;
    }

    public void setConnectedAccounts(long connectedAccounts) {
        this.connectedAccounts = connectedAccounts;
    }

    public Map<String, Long> getPostsByPlatform() {
        return postsByPlatform;
    }

    public void setPostsByPlatform(Map<String, Long> postsByPlatform) {
        this.postsByPlatform = postsByPlatform;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(double successRate) {
        this.successRate = successRate;
    }
}
