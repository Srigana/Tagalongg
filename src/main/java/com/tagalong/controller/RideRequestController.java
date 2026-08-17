package com.tagalong.controller;

import com.stripe.exception.StripeException;
import com.tagalong.dto.RequestDtos.CreateRequestPayload;
import com.tagalong.dto.RequestDtos.RequestResponse;
import com.tagalong.entity.User;
import com.tagalong.repository.UserRepository;
import com.tagalong.service.RideRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RideRequestController {

    private final RideRequestService rideRequestService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<RequestResponse> requestToJoin(Authentication auth,
                                                           @RequestBody CreateRequestPayload payload) throws StripeException {
        User requester = userRepository.findByEmail(auth.getName()).orElseThrow();
        return ResponseEntity.ok(rideRequestService.requestToJoin(requester, payload.postId()));
    }

    @PostMapping("/{requestId}/accept")
    public ResponseEntity<RequestResponse> accept(@PathVariable UUID requestId) throws StripeException {
        return ResponseEntity.ok(rideRequestService.respondToRequest(requestId, true));
    }

    @PostMapping("/{requestId}/reject")
    public ResponseEntity<RequestResponse> reject(@PathVariable UUID requestId) throws StripeException {
        return ResponseEntity.ok(rideRequestService.respondToRequest(requestId, false));
    }
}
