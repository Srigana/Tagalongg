package com.tagalong.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.time.Instant;
import java.util.UUID;

public class PostDtos {

    public record CreatePostRequest(
            @NotBlank String origin,
            @NotBlank String destination,
            @Future Instant departureTime,
            @Positive Integer availableSlots,
            @Positive Double farePerSeat) {}

    public record PostResponse(
            UUID id, String origin, String destination, Instant departureTime,
            Integer availableSlots, Double farePerSeat, String status, String posterName) {}
}
