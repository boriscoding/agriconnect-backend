package com.example.agriconnect.service;


import com.example.agriconnect.classes.Message;
import com.example.agriconnect.classes.PartageDiscussion;
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
    // Pour la modification
    Message modifierMessage(Long id, String nouveauContenu);

    // Pour le transfert (on peut réutiliser enregistrerMessageComplet,
    // mais une méthode dédiée est plus propre)
    Message transfererMessage(Long messageId, Long expId, Long destId);

    // Pour récupérer tous les messages d'une conversation (déjà utilisée dans ton controller)
    List<Message> findConversation(Long u1, Long u2);
    Message save(Message message);
    public Message getDernierMessageDiscussion(Long u1, Long u2) ;
    // 👇 NOUVELLES MÉTHODES POUR LE PARTAGE 👇
    PartageDiscussion creerPartage(PartageDiscussion partage);
    PartageDiscussion getPartageActif(Long initiateurId, Long beneficiaireId, Long tiersId);

}
