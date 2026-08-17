package com.tagalong.dto;

import java.util.UUID;

public class RequestDtos {

    public record CreateRequestPayload(UUID postId) {}

    public record RequestResponse(
            UUID id, UUID postId, String status, Double fareAmount) {}
}
