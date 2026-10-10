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
public class InstagramPublisher implements SocialPublisher {

    private static final Logger log = LoggerFactory.getLogger(InstagramPublisher.class);

    @Value("${app.social.simulation-mode:true}")
    private boolean simulationMode;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public PlatformType getPlatform() {
        return PlatformType.INSTAGRAM;
    }

    @Override
    public PublishResult publish(Post post, SocialAccount account) {
        log.info("Publishing to Instagram for account: {} (simulation={})", account.getAccountName(), simulationMode);

        // Instagram Feed requires an image or video
        if (post.getMediaUrl() == null || post.getMediaUrl().isBlank()) {
            return PublishResult.failure("Instagram publishing requires an image or video attachment.");
        }

        if (simulationMode) {
            String shortcode = UUID.randomUUID().toString().replace("-", "").substring(0, 11);
            String simulatedId = "ig_" + System.currentTimeMillis() + "_" + shortcode;
            String simulatedUrl = "https://instagram.com/p/" + shortcode;
            return PublishResult.success(simulatedId, simulatedUrl);
        }

        try {
            String igUserId = account.getExternalAccountId() != null ? account.getExternalAccountId() : "me";

            // Step 1: Create Instagram Media Container
            String containerUrl = "https://graph.facebook.com/v19.0/" + igUserId + "/media";
            MultiValueMap<String, String> containerBody = new LinkedMultiValueMap<>();
            containerBody.add("image_url", post.getMediaUrl());
            containerBody.add("caption", post.getCaption());
            containerBody.add("access_token", account.getAccessToken());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> containerRequest = new HttpEntity<>(containerBody, headers);

            ResponseEntity<Map> containerResponse = restTemplate.postForEntity(containerUrl, containerRequest, Map.class);
            if (!containerResponse.getStatusCode().is2xxSuccessful() || containerResponse.getBody() == null) {
                return PublishResult.failure("Failed to create Instagram media container.");
            }

            String creationId = (String) containerResponse.getBody().get("id");

            // Step 2: Publish Media Container
            String publishUrl = "https://graph.facebook.com/v19.0/" + igUserId + "/media_publish";
            MultiValueMap<String, String> publishBody = new LinkedMultiValueMap<>();
            publishBody.add("creation_id", creationId);
            publishBody.add("access_token", account.getAccessToken());

            HttpEntity<MultiValueMap<String, String>> publishRequest = new HttpEntity<>(publishBody, headers);
            ResponseEntity<Map> publishResponse = restTemplate.postForEntity(publishUrl, publishRequest, Map.class);

            if (publishResponse.getStatusCode().is2xxSuccessful() && publishResponse.getBody() != null) {
                String mediaId = (String) publishResponse.getBody().get("id");
                String postUrl = "https://instagram.com/p/" + mediaId;
                return PublishResult.success(mediaId, postUrl);
            } else {
                return PublishResult.failure("Failed to publish Instagram container: " + publishResponse.getStatusCode());
            }

        } catch (Exception e) {
            log.error("Failed to publish to Instagram: {}", e.getMessage(), e);
            return PublishResult.failure("Instagram API error: " + e.getMessage());
        }
    }
}
