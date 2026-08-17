package com.tagalong.event;

import java.util.UUID;

public record RequestMadeEvent(UUID requestId, UUID postId, UUID requesterId) {
}
