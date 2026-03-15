package com.example.agriconnect.service;


import com.example.agriconnect.classes.Message;
import com.example.agriconnect.classes.Utilisateur;
import com.example.agriconnect.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@Service
public interface MessageService {
    Message envoyerMessage(Message message);
    List<Message> getMessagesRecus(Long destinataireId);
    List<Message> getMessagesEnvoyes(Long expediteurId);
    Message getMessageById(Long id);
    void supprimerMessage(Long id);
    // Dans MessageService.java
    Message enregistrerMessageComplet(Message message, Long expId, Long destId);
    // Ajoute bien "throws IOException" ici
    String stockerFichier(MultipartFile file) throws IOException;
    // Tes autres méthodes...
    List<Utilisateur> findActiveContacts(Long id);
}
