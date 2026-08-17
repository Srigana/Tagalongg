package com.tagalong.service;

import com.tagalong.dto.PostDtos.CreatePostRequest;
import com.tagalong.dto.PostDtos.PostResponse;
import com.tagalong.entity.Post;
import com.tagalong.entity.User;
import com.tagalong.event.PostCreatedEvent;
import com.tagalong.kafka.TagAlongEventProducer;
import com.tagalong.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final TagAlongEventProducer eventProducer;

    public PostResponse createPost(User poster, CreatePostRequest req) {
        Post post = new Post();
        post.setPoster(poster);
        post.setOrigin(req.origin());
        post.setDestination(req.destination());
        post.setDepartureTime(req.departureTime());
        post.setAvailableSlots(req.availableSlots());
        post.setFarePerSeat(req.farePerSeat());

        Post saved = postRepository.save(post);

        eventProducer.publishPostCreated(
                new PostCreatedEvent(saved.getId(), saved.getDestination(), saved.getAvailableSlots()));

        return toResponse(saved);
    }

    // Cached per destination; evicted on new posts / accepted requests (see TagAlongEventConsumer)
    @Cacheable(value = "postSearch", key = "#destination.toLowerCase()")
    public List<PostResponse> searchByDestination(String destination) {
        return postRepository.findByDestinationIgnoreCaseAndStatus(destination, Post.PostStatus.ACTIVE)
                .stream().map(this::toResponse).toList();
    }

    private PostResponse toResponse(Post p) {
        return new PostResponse(
                p.getId(), p.getOrigin(), p.getDestination(), p.getDepartureTime(),
                p.getAvailableSlots(), p.getFarePerSeat(), p.getStatus().name(),
                p.getPoster().getFullName());
    }
}
