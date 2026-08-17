package com.tagalong.event;

import java.util.UUID;

public record PostCreatedEvent(UUID postId, String destination, Integer availableSlots) {
}
