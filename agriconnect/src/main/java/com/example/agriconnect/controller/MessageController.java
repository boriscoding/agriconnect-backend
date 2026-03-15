package com.example.agriconnect.controller;

import com.example.agriconnect.classes.Message;
import com.example.agriconnect.classes.Utilisateur;
import com.example.agriconnect.service.MessageServiceImplement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate; // ✅ Import pour le temps réel
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(
        origins = {
                "http://localhost:4200",
                "http://172.27.208.1:4200",
                "http://192.168.56.1:4200",
                "http://192.168.197.1:4200",
                "http://192.168.226.1:4200",
                "http://10.177.225.196:4200",
                "http://10.177.225.196:4200/",
                "http://10.101.75.196:4200/"
        },
        allowCredentials = "true"
)
@RequiredArgsConstructor
public class MessageController {

    private final MessageServiceImplement messageService;

    // ✅ Injection du template pour envoyer des messages via WebSocket
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * ENVOYER UN MESSAGE (Texte + Fichier optionnel)
     */
    @PostMapping(value = "/envoyer", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> envoyerMessage(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("expediteurId") Long expediteurId,
            @RequestParam("destinataireId") Long destinataireId,
            @RequestParam(value = "contenu", required = false) String contenu,
            @RequestParam("mediaType") String mediaType) {

        try {
            Message message = new Message();
            message.setContenu(contenu);
            message.setMediaType(mediaType);

            // Gestion du stockage de fichier (Image, Audio, Vidéo)
            if (file != null && !file.isEmpty()) {
                String fileUrl = messageService.stockerFichier(file);
                message.setMediaUrl(fileUrl);
            }

            // Sauvegarde en base de données
            Message sauvegarde = messageService.enregistrerMessageComplet(message, expediteurId, destinataireId);

            // ✅ DIFFUSION TEMPS RÉEL
            // Une fois le message en base (et le fichier stocké), on le pousse sur le topic.
            // Ton Angular recevra cet objet via son subscribe('/topic/messages')
            messagingTemplate.convertAndSend("/topic/messages", sauvegarde);

            return ResponseEntity.ok(sauvegarde);

        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    // --- 📥 MESSAGES REÇUS PAR UN UTILISATEUR ---
    @GetMapping("/recus/{destinataireId}")
    public ResponseEntity<List<Message>> getMessagesRecus(@PathVariable Long destinataireId) {
        return ResponseEntity.ok(messageService.getMessagesRecus(destinataireId));
    }

    // --- 📤 MESSAGES ENVOYÉS PAR UN UTILISATEUR ---
    @GetMapping("/envoyes/{expediteurId}")
    public ResponseEntity<List<Message>> getMessagesEnvoyes(@PathVariable Long expediteurId) {
        return ResponseEntity.ok(messageService.getMessagesEnvoyes(expediteurId));
    }

    // --- 🔍 RÉCUPÉRER UN MESSAGE PRÉCIS ---
    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessage(@PathVariable Long id) {
        Message msg = messageService.getMessageById(id);
        return msg != null ? ResponseEntity.ok(msg) : ResponseEntity.notFound().build();
    }

    // --- 🗑️ SUPPRIMER UN MESSAGE ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerMessage(@PathVariable Long id) {
        messageService.supprimerMessage(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/contacts/{id}")
    public ResponseEntity<List<Utilisateur>> getContacts(@PathVariable Long id) {
        List<Utilisateur> contacts = messageService.findActiveContacts(id);
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/conversation")
    public ResponseEntity<List<Message>> getConversation(
            @RequestParam("u1") Long u1,
            @RequestParam("u2") Long u2) {

        List<Message> messages = messageService.findConversation(u1, u2);
        return ResponseEntity.ok(messages);
    }
}