package com.socialsync.service;

import com.socialsync.dto.ConnectAccountRequest;
import com.socialsync.dto.SocialAccountDto;
import com.socialsync.model.PlatformType;
import com.socialsync.model.SocialAccount;
import com.socialsync.repository.SocialAccountRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final SocialAccountRepository accountRepository;

    public AccountService(SocialAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PostConstruct
    public void seedInitialAccounts() {
        if (accountRepository.count() == 0) {
            SocialAccount fb = new SocialAccount(
                    1L,
                    PlatformType.FACEBOOK,
                    "TechPulse Official Page",
                    "@techpulse_page",
                    "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=150",
                    "EAAG...sample_token",
                    "104928374928"
            );

            SocialAccount ig = new SocialAccount(
                    1L,
                    PlatformType.INSTAGRAM,
                    "TechPulse Daily",
                    "@techpulse_daily",
                    "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150",
                    "IGQV...sample_token",
                    "178414001234567"
            );

            SocialAccount li = new SocialAccount(
                    1L,
                    PlatformType.LINKEDIN,
                    "TechPulse Inc.",
                    "@techpulse-inc",
                    "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=150",
                    "AQU...sample_token",
                    "urn:li:organization:998822"
            );

            SocialAccount tw = new SocialAccount(
                    1L,
                    PlatformType.TWITTER,
                    "TechPulse HQ",
                    "@TechPulseHQ",
                    "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150",
                    "2849...sample_token",
                    "88991122"
            );

            accountRepository.saveAll(List.of(fb, ig, li, tw));
        }
    }

    @Transactional(readOnly = true)
    public List<SocialAccountDto> getAccounts(Long userId) {
        List<SocialAccount> list = (userId != null)
                ? accountRepository.findByUserIdAndActiveTrue(userId)
                : accountRepository.findByActiveTrue();

        return list.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public SocialAccountDto connectAccount(ConnectAccountRequest request) {
        SocialAccount account = new SocialAccount(
                request.getUserId(),
                request.getPlatform(),
                request.getAccountName(),
                request.getHandle(),
                request.getAvatarUrl() != null && !request.getAvatarUrl().isBlank()
                        ? request.getAvatarUrl()
                        : "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150",
                request.getAccessToken() != null ? request.getAccessToken() : "token_" + System.currentTimeMillis(),
                request.getExternalAccountId() != null ? request.getExternalAccountId() : "ext_" + System.currentTimeMillis()
        );

        SocialAccount saved = accountRepository.save(account);
        return toDto(saved);
    }

    @Transactional
    public void disconnectAccount(Long id) {
        accountRepository.findById(id).ifPresent(account -> {
            account.setActive(false);
            accountRepository.save(account);
        });
    }

    public SocialAccountDto toDto(SocialAccount account) {
        SocialAccountDto dto = new SocialAccountDto();
        dto.setId(account.getId());
        dto.setPlatform(account.getPlatform());
        dto.setAccountName(account.getAccountName());
        dto.setHandle(account.getHandle());
        dto.setAvatarUrl(account.getAvatarUrl());
        dto.setExternalAccountId(account.getExternalAccountId());
        dto.setActive(account.isActive());
        dto.setConnectedAt(account.getConnectedAt());
        return dto;
    }
}
