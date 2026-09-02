package com.example.agriconnect.controller;

import com.example.agriconnect.classes.Message;
import com.example.agriconnect.classes.PartageDiscussion;
import com.example.agriconnect.classes.Utilisateur;
import com.example.agriconnect.service.MessageServiceImplement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                "http://10.101.75.196:4200/",
                "https://unsacked-improvisationally-suanne.ngrok-free.dev",
                "https://agrilinkbycam.netlify.app"
        },
        allowCredentials = "true"
)
@RequiredArgsConstructor
public class MessageController {

    private final MessageServiceImplement messageService;
    private final SimpMessagingTemplate messagingTemplate;

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

            if (file != null && !file.isEmpty()) {
                String fileUrl = messageService.stockerFichier(file);
                message.setMediaUrl(fileUrl);
            }

            Message sauvegarde = messageService.enregistrerMessageComplet(message, expediteurId, destinataireId);
            messagingTemplate.convertAndSend("/topic/messages", sauvegarde);
            return ResponseEntity.ok(sauvegarde);

        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("/recus/{destinataireId}")
    public ResponseEntity<List<Message>> getMessagesRecus(@PathVariable Long destinataireId) {
        return ResponseEntity.ok(messageService.getMessagesRecus(destinataireId));
    }

    @GetMapping("/envoyes/{expediteurId}")
    public ResponseEntity<List<Message>> getMessagesEnvoyes(@PathVariable Long expediteurId) {
        return ResponseEntity.ok(messageService.getMessagesEnvoyes(expediteurId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessage(@PathVariable Long id) {
        Message msg = messageService.getMessageById(id);
        return msg != null ? ResponseEntity.ok(msg) : ResponseEntity.notFound().build();
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerMessage(@PathVariable Long id) {
        messageService.supprimerMessage(id);
        messagingTemplate.convertAndSend("/topic/messages/delete", id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/modifier/{id}")
    public ResponseEntity<Message> modifierMessage(@PathVariable Long id, @RequestBody String nouveauContenu) {
        Message msg = messageService.getMessageById(id);
        if (msg == null) return ResponseEntity.notFound().build();

        msg.setContenu(nouveauContenu);
        Message sauvegarde = messageService.save(msg);
        messagingTemplate.convertAndSend("/topic/messages/update", sauvegarde);

        return ResponseEntity.ok(sauvegarde);
    }

    @PostMapping("/transferer")
    public ResponseEntity<Message> transfererMessage(
            @RequestParam("messageId") Long messageId,
            @RequestParam("expediteurId") Long expediteurId,
            @RequestParam("destinataireId") Long destinataireId) {

        Message original = messageService.getMessageById(messageId);
        if (original == null) return ResponseEntity.notFound().build();

        Message copie = new Message();
        copie.setContenu(original.getContenu());
        copie.setMediaUrl(original.getMediaUrl());
        copie.setMediaType(original.getMediaType());

        Message sauvegarde = messageService.enregistrerMessageComplet(copie, expediteurId, destinataireId);
        messagingTemplate.convertAndSend("/topic/messages", sauvegarde);

        return ResponseEntity.ok(sauvegarde);
    }

    @GetMapping("/dernier-echange")
    public ResponseEntity<Message> getDernierMessage(@RequestParam Long u1, @RequestParam Long u2) {
        Message dernierMsg = messageService.getDernierMessageDiscussion(u1, u2);
        if (dernierMsg == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dernierMsg);
    }

    // --- 👇 PARTAGE DE DISCUSSION 👇 ---

    @PostMapping("/partage/creer")
    public ResponseEntity<PartageDiscussion> creerPartage(
            @RequestParam Long initiateurId,
            @RequestParam Long beneficiaireId,
            @RequestParam Long tiersId,
            @RequestParam String dateDebut) {

        PartageDiscussion partage = new PartageDiscussion();
        partage.setInitiateurId(initiateurId);
        partage.setBeneficiaireId(beneficiaireId);
        partage.setTiersId(tiersId);
        partage.setDateDebutAcces(LocalDateTime.parse(dateDebut));

        PartageDiscussion saved = messageService.creerPartage(partage);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/partage/conversation")
    public ResponseEntity<List<Map<String, Object>>> getConversationPartagee(
            @RequestParam Long beneficiaireId,
            @RequestParam Long initiateurId,
            @RequestParam Long tiersId) {

        PartageDiscussion partage = messageService.getPartageActif(initiateurId, beneficiaireId, tiersId);
        if(partage == null) {
            return ResponseEntity.status(403).build();
        }

        List<Message> messages = messageService.findConversation(initiateurId, tiersId);

        List<Map<String, Object>> messagesFiltres = messages.stream()
                // ✅ Utilisation de getTimestamp() qui correspond à ton modèle Message
                .filter(m -> m.getTimestamp() != null && !m.getTimestamp().isBefore(partage.getDateDebutAcces()))
                .map(m -> {
                    Map<String, Object> msgMap = new HashMap<>();
                    msgMap.put("id", m.getId());
                    msgMap.put("contenu", m.getContenu());
                    msgMap.put("mediaType", m.getMediaType());
                    msgMap.put("mediaUrl", m.getMediaUrl());
                    msgMap.put("timestamp", m.getTimestamp());
                    msgMap.put("destinataire", m.getDestinataire());

                    if (m.getExpediteur() != null && m.getExpediteur().getId().equals(tiersId)) {
                        Map<String, Object> userMasque = new HashMap<>();
                        userMasque.put("id", tiersId);
                        userMasque.put("prenom", "Contact");
                        userMasque.put("nom", "Masqué");

                        msgMap.put("expediteur", userMasque);
                    } else {
                        msgMap.put("expediteur", m.getExpediteur());
                    }

                    return msgMap;
                }).collect(Collectors.toList());

        return ResponseEntity.ok(messagesFiltres);
    }

    @PostMapping("/partage/envoyer")
    public ResponseEntity<Message> envoyerMessageDelegue(
            @RequestParam Long beneficiaireId,
            @RequestParam Long initiateurId,
            @RequestParam Long destinataireId,
            @RequestParam String contenu) {

        Message message = new Message();
        message.setContenu(contenu);
        message.setMediaType("TEXT");

        Message sauvegarde = messageService.enregistrerMessageComplet(message, initiateurId, destinataireId);
        messagingTemplate.convertAndSend("/topic/messages", sauvegarde);

        return ResponseEntity.ok(sauvegarde);
    }
}