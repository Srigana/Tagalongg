package com.tagalong.service;

import com.tagalong.dto.RequestDtos.RequestResponse;
import com.tagalong.entity.Post;
import com.tagalong.entity.RideRequest;
import com.tagalong.entity.User;
import com.tagalong.event.RequestMadeEvent;
import com.tagalong.event.StatusUpdatedEvent;
import com.tagalong.kafka.TagAlongEventProducer;
import com.tagalong.repository.PostRepository;
import com.tagalong.repository.RideRequestRepository;
import com.stripe.exception.StripeException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RideRequestService {

    private final RideRequestRepository requestRepository;
    private final PostRepository postRepository;
    private final StripeService stripeService;
    private final TagAlongEventProducer eventProducer;

    @Transactional
    public RequestResponse requestToJoin(User requester, UUID postId) throws StripeException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        RideRequest request = new RideRequest();
        request.setPost(post);
        request.setRequester(requester);
        request.setFareAmount(post.getFarePerSeat());

        // Hold funds now; captured only if the poster accepts
        String paymentIntentId = stripeService.holdPayment(post.getFarePerSeat(), requester.getEmail());
        request.setStripePaymentIntentId(paymentIntentId);

        RideRequest saved = requestRepository.save(request);

        eventProducer.publishRequestMade(
                new RequestMadeEvent(saved.getId(), post.getId(), requester.getId()));

        return toResponse(saved);
    }

    @Transactional
    public RequestResponse respondToRequest(UUID requestId, boolean accept) throws StripeException {
        RideRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        if (accept) {
            stripeService.capturePayment(request.getStripePaymentIntentId());
            request.setStatus(RideRequest.RequestStatus.ACCEPTED);
        } else {
            stripeService.releasePayment(request.getStripePaymentIntentId());
            request.setStatus(RideRequest.RequestStatus.REJECTED);
        }

        RideRequest saved = requestRepository.save(request);

        eventProducer.publishStatusUpdated(
                new StatusUpdatedEvent(saved.getId(), saved.getPost().getId(), saved.getStatus().name()));

        return toResponse(saved);
    }

    private RequestResponse toResponse(RideRequest r) {
        return new RequestResponse(r.getId(), r.getPost().getId(), r.getStatus().name(), r.getFareAmount());
    }
}
