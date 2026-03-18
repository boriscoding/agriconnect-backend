package com.example.agriconnect.service;

import com.example.agriconnect.classes.Message;
import com.example.agriconnect.classes.Utilisateur;
import com.example.agriconnect.repository.MessageRepository;
import com.example.agriconnect.repository.UtilisateurRepository; // Ajout important
import lombok.RequiredArgsConstructor;
// ✅ Utilisez cet import
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImplement implements MessageService {

    private final MessageRepository messageRepository;
    private final UtilisateurRepository utilisateurRepository; // Pour récupérer les vrais Utilisateurs

    @Override
    public Message envoyerMessage(Message message) {
        // Cette méthode simple fonctionne si l'objet message est déjà complet
        return messageRepository.save(message);
    }

    // Cette méthode est celle que ton Controller doit appeler pour lier les IDs
    public Message enregistrerMessageComplet(Message message, Long expId, Long destId) {
        Utilisateur exp = utilisateurRepository.findById(expId)
                .orElseThrow(() -> new RuntimeException("Expéditeur non trouvé"));
        Utilisateur dest = utilisateurRepository.findById(destId)
                .orElseThrow(() -> new RuntimeException("Destinataire non trouvé"));

        message.setExpediteur(exp);
        message.setDestinataire(dest);

        return messageRepository.save(message);
    }

    // Dans MessageServiceImplement.java
    @Override
    public String stockerFichier(MultipartFile file) throws IOException {
        // 1. Définir le dossier (il sera créé à la racine du projet)
        String uploadFolder = "uploads/";
        java.nio.file.Path uploadPath = java.nio.file.Paths.get(uploadFolder);

        if (!java.nio.file.Files.exists(uploadPath)) {
            java.nio.file.Files.createDirectories(uploadPath);
        }

        // 2. Générer un nom unique (UUID) pour éviter les doublons
        String originalFileName = file.getOriginalFilename();
        String extension = (originalFileName != null && originalFileName.contains("."))
                ? originalFileName.substring(originalFileName.lastIndexOf(".")) : "";
        String fileName = java.util.UUID.randomUUID().toString() + extension;

        // 3. Sauvegarder le fichier
        java.nio.file.Path filePath = uploadPath.resolve(fileName);
        java.nio.file.Files.copy(file.getInputStream(), filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        // 4. Retourner l'URL pour la base de données
        return "http://localhost:8080/uploads/" + fileName;
    }
    @Override
    public List<Message> getMessagesRecus(Long destinataireId) {
        // Attention aux majuscules pour correspondre au Repository corrigé
        return messageRepository.findByDestinataire_Id(destinataireId);
    }

    @Override
    public List<Message> getMessagesEnvoyes(Long expediteurId) {
        return messageRepository.findByExpediteur_Id(expediteurId);
    }

    @Override
    public Message getMessageById(Long id) {
        return messageRepository.findById(id).orElse(null);
    }

    @Override
    public void supprimerMessage(Long id) {
        messageRepository.deleteById(id);
    }
    @Override
    public List<Utilisateur> findActiveContacts(Long id) {
        return messageRepository.findActiveContacts(id);
    }
    public List<Message> getConversation(Long user1Id, Long user2Id) {
        // Récupère tous les messages entre deux personnes, triés par date
        // On cherche (A vers B) OU (B vers A)
        return messageRepository.findConversation(user1Id, user2Id);
    }

    public List<Message> findConversation(Long u1, Long u2) {
        return messageRepository.findConversation(u1, u2);
    }
    @Override
    public Message modifierMessage(Long id, String nouveauContenu) {
        Message msg = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message introuvable"));

        msg.setContenu(nouveauContenu);
        // Optionnel : msg.setModifie(true); // Si tu as ajouté ce champ dans ta classe Message

        return messageRepository.save(msg); // Ici, le .save() vient du MessageRepository (JPA)
    }

    @Override
    public Message transfererMessage(Long messageId, Long expId, Long destId) {
        Message original = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message original introuvable"));

        Message copie = new Message();
        copie.setContenu(original.getContenu());
        copie.setMediaUrl(original.getMediaUrl());
        copie.setMediaType(original.getMediaType());

        // On réutilise ta méthode existante pour gérer les liaisons Utilisateurs
        return enregistrerMessageComplet(copie, expId, destId);
    }
    @Override
    public Message save(Message message) {
        return messageRepository.save(message); // repo est ton MessageRepository
    }

    public Message getDernierMessageDiscussion(Long u1, Long u2) {
        List<Message> messages = messageRepository.findLastMessageBetween(u1, u2, PageRequest.of(0, 1));
        return messages.isEmpty() ? null : messages.get(0);
    }
}