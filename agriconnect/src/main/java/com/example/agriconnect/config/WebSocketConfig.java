package com.example.agriconnect.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Le broker "topic" servira à envoyer les messages vers le client
        config.enableSimpleBroker("/topic");
        // Le préfixe "app" servira à recevoir les messages du client
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Point d'entrée de la connexion WebSocket
        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*") // Indispensable pour éviter les erreurs CORS
                .withSockJS();
    }
}