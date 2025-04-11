package org.example.travelexpertwebbackend.controller;

import org.example.travelexpertwebbackend.entity.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.tinylog.Logger;


@Controller
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.send")
    public void handleChatMessage(@Payload ChatMessage message) {
        // print the message to the console
        Logger.debug("Chat message received: " + message);
        ChatMessage chatMessage = new ChatMessage(
                message.getSenderId(),
                message.getReceiverId(),
                message.getContent()
        );
        messagingTemplate.convertAndSendToUser(message.getReceiverId(), "/queue/messages", chatMessage);
    }
}
