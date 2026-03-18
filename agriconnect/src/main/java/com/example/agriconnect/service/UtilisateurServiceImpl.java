package com.example.agriconnect.service;

import com.example.agriconnect.classes.Producteur;
import com.example.agriconnect.classes.Transporteur;
import com.example.agriconnect.classes.Utilisateur;
import com.example.agriconnect.repository.UtilisateurRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public Optional<Utilisateur> findById(Long id) {
        return utilisateurRepository.findById(id);
    }

    @Override
    public Optional<Utilisateur> getProfil(Long id) {
        return utilisateurRepository.findById(id);
    }

    @Override
    public Utilisateur modifierProfil(Long id, Utilisateur userDetails) {
        return null; // Implémentation si nécessaire
    }

    @Override
    @Transactional
    public Utilisateur modifierProfil(Long id, Map<String, Object> updates) {
        return utilisateurRepository.findById(id).map(user -> {
            updateCommonFields(user, updates);
            return utilisateurRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'ID : " + id));
    }

    @Override
    @Transactional
    public Utilisateur modifierProfilAvecImage(Long id, Map<String, Object> updates, MultipartFile file) {
        return utilisateurRepository.findById(id).map(user -> {
            // 1. Mise à jour des champs texte
            updateCommonFields(user, updates);

            // 2. Gestion sécurisée du fichier physique
            if (file != null && !file.isEmpty()) {
                try {
                    // RÉSOLUTION DU BUG DE SYNTAXE : On nettoie le nom du fichier
                    String originalName = file.getOriginalFilename();
                    String extension = "";
                    if (originalName != null && originalName.contains(".")) {
                        extension = originalName.substring(originalName.lastIndexOf("."));
                    }

                    // On utilise uniquement l'UUID pour le nom physique afin d'éviter les emojis/caractères spéciaux
                    String fileName = UUID.randomUUID().toString() + extension;

                    // Chemin absolu défini dans ton WebConfig
                    Path path = Paths.get("C:/Users/User/Documents/agriconnect (1)/uploads/" + fileName);

                    // Écriture physique (le bloc try/catch résout l'Unhandled Exception)
                    Files.write(path, file.getBytes());

                    // Sauvegarde du chemin relatif pour la restitution
                    user.setPhotosuser("/uploads/" + fileName);

                } catch (IOException e) {
                    // Log précis pour le débogage serveur
                    throw new RuntimeException("Erreur de stockage physique : " + e.getMessage());
                }
            }

            Utilisateur saved = utilisateurRepository.save(user);

            // 3. Notification WebSocket pour mise à jour immédiate du chat
            messagingTemplate.convertAndSend("/topic/user-update/" + id, saved);

            return saved;
        }).orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }

    /**
     * Méthode utilitaire pour centraliser la mise à jour des champs communs
     */
    private void updateCommonFields(Utilisateur user, Map<String, Object> updates) {
        if (updates.get("nom") != null) user.setNom((String) updates.get("nom"));
        if (updates.get("localisation") != null) user.setLocalisation((String) updates.get("localisation"));
        if (updates.get("sexe") != null) user.setSexe((String) updates.get("sexe"));

        if (updates.get("number") != null) {
            user.setNumber(Integer.valueOf(updates.get("number").toString()));
        }

        // Logique spécifique Producteur
        if (user instanceof Producteur p) {
            if (updates.get("typeProduit") != null) p.setTypeProduit((String) updates.get("typeProduit"));
            if (updates.get("surfaceExploitation") != null) {
                p.setSurfaceExploitation(Double.valueOf(updates.get("surfaceExploitation").toString()));
            }
        }

        // Logique spécifique Transporteur
        if (user instanceof Transporteur t) {
            if (updates.get("typeVehicule") != null) t.setTypeVehicule((String) updates.get("typeVehicule"));
            if (updates.get("capaciteCharge") != null) {
                t.setCapaciteMax(Double.valueOf(updates.get("capaciteCharge").toString()));
            }
        }
    }
}