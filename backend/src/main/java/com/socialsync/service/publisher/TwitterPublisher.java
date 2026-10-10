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
public class TwitterPublisher implements SocialPublisher {

    private static final Logger log = LoggerFactory.getLogger(TwitterPublisher.class);

    @Value("${app.social.simulation-mode:true}")
    private boolean simulationMode;

    @Override
    public PlatformType getPlatform() {
        return PlatformType.TWITTER;
    }

    @Override
    public PublishResult publish(Post post, SocialAccount account) {
        log.info("Publishing to X/Twitter for account: {} (simulation={})", account.getAccountName(), simulationMode);

        // Character limit check for Twitter free tier (280 characters)
        if (post.getCaption() != null && post.getCaption().length() > 280) {
            return PublishResult.failure("Twitter character limit exceeded (max 280 characters). Your post was " + post.getCaption().length() + " characters.");
        }

        if (simulationMode) {
            String tweetId = String.valueOf(1700000000000000000L + (long)(Math.random() * 9999999999L));
            String handleClean = account.getHandle().replace("@", "");
            String postUrl = "https://x.com/" + handleClean + "/status/" + tweetId;
            return PublishResult.success(tweetId, postUrl);
        }

        return PublishResult.success("tw_" + System.currentTimeMillis(), "https://x.com");
    }
}
