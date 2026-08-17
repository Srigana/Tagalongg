package com.tagalong.kafka;

import com.tagalong.entity.Post;
import com.tagalong.event.RequestMadeEvent;
import com.tagalong.event.StatusUpdatedEvent;
import com.tagalong.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Reacts to events independently of the request thread that created them —
 * mirrors the original design where a request triggers a consumer that
 * updates slot availability without blocking the API response.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TagAlongEventConsumer {

    private final PostRepository postRepository;

    @KafkaListener(topics = "${kafka.topics.request-made}", groupId = "tagalong-service")
    @CacheEvict(value = "postSearch", allEntries = true)
    public void onRequestMade(RequestMadeEvent event) {
        log.info("Request {} made for post {}", event.requestId(), event.postId());
        // Slot availability isn't decremented here — only on ACCEPT (see onStatusUpdated)
        // to avoid holding seats for requests that are never confirmed.
    }

    @KafkaListener(topics = "${kafka.topics.status-updated}", groupId = "tagalong-service")
    @CacheEvict(value = "postSearch", allEntries = true)
    public void onStatusUpdated(StatusUpdatedEvent event) {
        if ("ACCEPTED".equals(event.newStatus())) {
            postRepository.findById(event.postId()).ifPresent(post -> {
                int remaining = post.getAvailableSlots() - 1;
                post.setAvailableSlots(Math.max(remaining, 0));
                if (remaining <= 0) {
                    post.setStatus(Post.PostStatus.FULL);
                }
                postRepository.save(post);
                log.info("Post {} slots updated to {}", post.getId(), post.getAvailableSlots());
            });
        }
    }
}
