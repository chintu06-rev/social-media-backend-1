package com.socialsync.service.publisher;

import com.socialsync.model.PlatformType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class PublisherFactory {

    private final Map<PlatformType, SocialPublisher> publisherMap = new EnumMap<>(PlatformType.class);

    public PublisherFactory(List<SocialPublisher> publishers) {
        for (SocialPublisher publisher : publishers) {
            publisherMap.put(publisher.getPlatform(), publisher);
        }
    }

    public SocialPublisher getPublisher(PlatformType platform) {
        SocialPublisher publisher = publisherMap.get(platform);
        if (publisher == null) {
            throw new IllegalArgumentException("No publisher registered for platform: " + platform);
        }
        return publisher;
    }
}
