package com.tagalong.event;

import java.util.UUID;

public record StatusUpdatedEvent(UUID requestId, UUID postId, String newStatus) {
}
