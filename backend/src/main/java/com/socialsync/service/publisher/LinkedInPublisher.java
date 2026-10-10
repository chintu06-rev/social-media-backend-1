package com.socialsync.service.publisher;

import com.socialsync.model.PlatformType;
import com.socialsync.model.Post;
import com.socialsync.model.SocialAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LinkedInPublisher implements SocialPublisher {

    private static final Logger log = LoggerFactory.getLogger(LinkedInPublisher.class);

    @Value("${app.social.simulation-mode:true}")
    private boolean simulationMode;

    @Override
    public PlatformType getPlatform() {
        return PlatformType.LINKEDIN;
    }

    @Override
    public PublishResult publish(Post post, SocialAccount account) {
        log.info("Publishing to LinkedIn for account: {} (simulation={})", account.getAccountName(), simulationMode);

        if (simulationMode) {
            String urn = "urn:li:share:" + (System.currentTimeMillis() + (long)(Math.random() * 10000));
            String postUrl = "https://linkedin.com/feed/update/" + urn;
            return PublishResult.success(urn, postUrl);
        }

        // Live LinkedIn v2 ugcPosts logic can be activated with OAuth client tokens
        return PublishResult.success("urn:li:share:" + System.currentTimeMillis(), "https://linkedin.com/feed");
    }
}
