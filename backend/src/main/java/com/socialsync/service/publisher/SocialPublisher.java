package com.socialsync.service.publisher;

import com.socialsync.model.PlatformType;
import com.socialsync.model.Post;
import com.socialsync.model.SocialAccount;

public interface SocialPublisher {
    PlatformType getPlatform();
    PublishResult publish(Post post, SocialAccount account);
}
