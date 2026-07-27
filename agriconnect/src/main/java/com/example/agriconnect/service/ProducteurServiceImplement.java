package com.example.agriconnect.service;

import com.example.agriconnect.classes.Producteur;
import com.example.agriconnect.repository.ProducteurRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProducteurServiceImplement implements ProducteurService {

    private final ProducteurRepository producteurRepository;

    @Autowired // ✅ Indispensable pour la notification en temps réel
    private SimpMessagingTemplate messagingTemplate;

    public ProducteurServiceImplement(ProducteurRepository producteurRepository) {
        this.producteurRepository = producteurRepository;
    }

    @Override
    public Producteur findByEmailAndPassword(String email, String password) {
        // Recherche par email (attention à la sensibilité à la casse en base)
        List<Producteur> producteurs = producteurRepository.findAllByEmail(email);

        if (producteurs.isEmpty()) {
            throw new RuntimeException("Email non trouvé");
        }

        Producteur p = producteurs.get(0);
        if (p.getPassword().equals(password)) {
            return p;
        } else {
            throw new RuntimeException("Mot de passe incorrect");
        }
    }

    @Override
    @Transactional
    public Producteur modifierProfilAvecImage(Long id, Map<String, Object> updates, MultipartFile file) {
        return producteurRepository.findById(id).map(producteur -> {

            // 1. Mise à jour des champs communs (Hérités de Utilisateur)
            if (updates.get("nom") != null) producteur.setNom((String) updates.get("nom"));
            if (updates.get("localisation") != null) producteur.setLocalisation((String) updates.get("localisation"));

            // Conversion sécurisée de l'Integer (number)
            if (updates.get("number") != null) {
                producteur.setNumber(Integer.valueOf(updates.get("number").toString()));
            }

            // 2. Mise à jour des champs spécifiques (Producteur)
            if (updates.get("typeProduit") != null) producteur.setTypeProduit((String) updates.get("typeProduit"));

            // Conversion sécurisée du Double (surfaceExploitation)
            if (updates.get("surfaceExploitation") != null) {
                producteur.setSurfaceExploitation(Double.valueOf(updates.get("surfaceExploitation").toString()));
            }

            // 3. Gestion physique de l'image (Plus besoin de FileStorageService extérieur)
            if (file != null && !file.isEmpty()) {
                try {
                    String originalName = file.getOriginalFilename();
                    String extension = "";
                    if (originalName != null && originalName.contains(".")) {
                        extension = originalName.substring(originalName.lastIndexOf("."));
                    }

                    // On génère un nom unique pour éviter les doublons
                    String fileName = UUID.randomUUID().toString() + extension;

                    // On utilise le chemin absolu de ton projet AgriConnect
                    Path path = Paths.get("C:/Users/User/Documents/agriconnect (1)/uploads/" + fileName);

                    // Création du dossier si il n'existe pas
                    if (!Files.exists(path.getParent())) {
                        Files.createDirectories(path.getParent());
                    }

                    // Écriture du fichier sur le disque
                    Files.write(path, file.getBytes());

                    // Mise à jour du chemin en base de données
                    producteur.setPhotosuser("/uploads/" + fileName);

                } catch (IOException e) {
                    throw new RuntimeException("Erreur de stockage physique : " + e.getMessage());
                }
            }

            // 4. Sauvegarde
            Producteur saved = producteurRepository.save(producteur);

            // 5. Notification WebSocket (pour mettre à jour la photo dans le chat Angular)
            messagingTemplate.convertAndSend("/topic/user-update/" + id, saved);

            return saved;
        }).orElseThrow(() -> new RuntimeException("Producteur introuvable avec l'ID : " + id));
    }

    @Override
    public Producteur create(Producteur producteur) {
        return producteurRepository.save(producteur);
    }

    @Override
    public List<Producteur> findAll() {
        return producteurRepository.findAll();
    }

    @Override
    public Producteur findById(Long id) {
        return producteurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producteur non trouvé"));
    }

    @Override
    public Producteur update(Long id, Producteur nouveauProducteur) {
        Producteur producteur = findById(id);
        producteur.setTypeProduit(nouveauProducteur.getTypeProduit());
        producteur.setSurfaceExploitation(nouveauProducteur.getSurfaceExploitation());
        producteur.setNom(nouveauProducteur.getNom()); // N'oublie pas de mettre à jour le nom aussi ici
        return producteurRepository.save(producteur);
    }

    @Override
    public void delete(Long id) {
        producteurRepository.deleteById(id);
    }
}