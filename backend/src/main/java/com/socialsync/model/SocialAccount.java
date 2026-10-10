package com.socialsync.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "social_accounts")
public class SocialAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlatformType platform;

    @Column(nullable = false)
    private String accountName;

    @Column(nullable = false)
    private String handle;

    private String avatarUrl;

    @Column(length = 2048)
    private String accessToken;

    private String externalAccountId;

    private Long userId;

    private boolean active = true;

    private LocalDateTime connectedAt = LocalDateTime.now();

    public SocialAccount() {
    }

    public SocialAccount(Long userId, PlatformType platform, String accountName, String handle, String avatarUrl, String accessToken, String externalAccountId) {
        this.userId = userId;
        this.platform = platform;
        this.accountName = accountName;
        this.handle = handle;
        this.avatarUrl = avatarUrl;
        this.accessToken = accessToken;
        this.externalAccountId = externalAccountId;
        this.active = true;
        this.connectedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getExternalAccountId() {
        return externalAccountId;
    }

    public void setExternalAccountId(String externalAccountId) {
        this.externalAccountId = externalAccountId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getConnectedAt() {
        return connectedAt;
    }

    public void setConnectedAt(LocalDateTime connectedAt) {
        this.connectedAt = connectedAt;
    }
}
