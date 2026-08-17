package com.tagalong.kafka;

import com.tagalong.event.PostCreatedEvent;
import com.tagalong.event.RequestMadeEvent;
import com.tagalong.event.StatusUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Publishes domain events. Consumers (in this service or others) react
 * asynchronously — e.g. slot availability updates, search cache invalidation.
 */
@Component
@RequiredArgsConstructor
public class TagAlongEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.post-created}")
    private String postCreatedTopic;

    @Value("${kafka.topics.request-made}")
    private String requestMadeTopic;

    @Value("${kafka.topics.status-updated}")
    private String statusUpdatedTopic;

    public void publishPostCreated(PostCreatedEvent event) {
        kafkaTemplate.send(postCreatedTopic, event.postId().toString(), event);
    }

    public void publishRequestMade(RequestMadeEvent event) {
        kafkaTemplate.send(requestMadeTopic, event.postId().toString(), event);
    }

    public void publishStatusUpdated(StatusUpdatedEvent event) {
        kafkaTemplate.send(statusUpdatedTopic, event.postId().toString(), event);
    }
}
