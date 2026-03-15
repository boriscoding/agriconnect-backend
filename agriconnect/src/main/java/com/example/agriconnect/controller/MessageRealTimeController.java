package com.example.agriconnect.controller;

import com.example.agriconnect.classes.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageRealTimeController {

    @MessageMapping("/chat.sendMessage") // L'URL que le client Angular appellera
    @SendTo("/topic/messages")           // Le canal où tout le monde écoute
    public Message sendMessage(Message message) {
        // Ici, le message contient déjà l'id expediteur (ex: 1)
        return message;
    }
}