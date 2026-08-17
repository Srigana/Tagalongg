package com.tagalong.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.Instant;

/**
 * Real-time chat between poster and requester, scoped per request id.
 * Client sends to /app/chat/{requestId}, subscribes to /topic/chat/{requestId}.
 */
@Controller
public class ChatController {

    public record ChatMessage(String sender, String content, Instant sentAt) {}

    @MessageMapping("/chat/{requestId}")
    @SendTo("/topic/chat/{requestId}")
    public ChatMessage relay(@DestinationVariable String requestId, ChatMessage message) {
        return new ChatMessage(message.sender(), message.content(), Instant.now());
    }
}
