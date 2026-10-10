package com.socialsync.service.publisher;

import com.socialsync.model.PlatformType;
import com.socialsync.model.Post;
import com.socialsync.model.SocialAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Service
public class FacebookPublisher implements SocialPublisher {

    private static final Logger log = LoggerFactory.getLogger(FacebookPublisher.class);

    @Value("${app.social.simulation-mode:true}")
    private boolean simulationMode;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public PlatformType getPlatform() {
        return PlatformType.FACEBOOK;
    }

    @Override
    public PublishResult publish(Post post, SocialAccount account) {
        log.info("Publishing to Facebook for account: {} (simulation={})", account.getAccountName(), simulationMode);

        if (simulationMode) {
            String simulatedId = "fb_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 6);
            String simulatedUrl = "https://facebook.com/" + account.getHandle().replace("@", "") + "/posts/" + simulatedId;
            return PublishResult.success(simulatedId, simulatedUrl);
        }

        try {
            String pageId = account.getExternalAccountId() != null ? account.getExternalAccountId() : "me";
            String url;
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("access_token", account.getAccessToken());

            if (post.getMediaUrl() != null && !post.getMediaUrl().isBlank()) {
                url = "https://graph.facebook.com/v19.0/" + pageId + "/photos";
                body.add("url", post.getMediaUrl());
                body.add("caption", post.getCaption());
            } else {
                url = "https://graph.facebook.com/v19.0/" + pageId + "/feed";
                body.add("message", post.getCaption());
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String postId = (String) response.getBody().get("id");
                String postUrl = "https://facebook.com/" + postId;
                return PublishResult.success(postId, postUrl);
            } else {
                return PublishResult.failure("Facebook API returned status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Failed to publish to Facebook: {}", e.getMessage(), e);
            return PublishResult.failure("Facebook publishing error: " + e.getMessage());
        }
    }
}
