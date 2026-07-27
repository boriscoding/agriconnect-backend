package com.example.agriconnect.service;

import com.example.agriconnect.classes.Acheteur;
import com.example.agriconnect.repository.AcheteurRepository;
import lombok.RequiredArgsConstructor;
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
public class AcheteurServiceImplement implements AcheteurService {

    private final AcheteurRepository acheteurRepository;
    // Chemin où les images seront stockées (à adapter selon ton projet)
    private final String uploadDir = "uploads/profiles/";

    public AcheteurServiceImplement(AcheteurRepository acheteurRepository) {
        this.acheteurRepository = acheteurRepository;
    }

    @Override
    public Acheteur findByEmailAndPassword(String email, String password) {
        return acheteurRepository.findByEmail(email)
                .filter(a -> a.getPassword().equals(password))
                .orElse(null); // Retourne null pour que le Controller renvoie un 401
    }

    @Override
    public Acheteur creerAcheteur(Acheteur acheteur, MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            acheteur.setPhotosuser(saveImage(file));
        }
        return acheteurRepository.save(acheteur);
    }

    @Override
    public Acheteur modifierProfil(Long id, Map<String, Object> updates, MultipartFile file) {
        Acheteur existant = getAcheteurById(id);

        // Mise à jour des champs (on ajoute adresseLivraison)
        if (updates.containsKey("nom")) existant.setNom((String) updates.get("nom"));
        if (updates.containsKey("email")) existant.setEmail((String) updates.get("email"));
        if (updates.containsKey("localisation")) existant.setLocalisation((String) updates.get("localisation"));
        if (updates.containsKey("number")) existant.setNumber((Integer) updates.get("number"));
        if (updates.containsKey("sexe")) existant.setSexe((String) updates.get("sexe"));
        if (updates.containsKey("adresseLivraison")) existant.setAdresseLivraison((String) updates.get("adresseLivraison"));

        // Gestion de l'image
        if (file != null && !file.isEmpty()) {
            existant.setPhotosuser(saveImage(file));
        }

        return acheteurRepository.save(existant);
    }

    // Méthode utilitaire pour sauvegarder l'image sur le disque
    private String saveImage(MultipartFile file) {
        try {
            Path path = Paths.get(uploadDir);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            // Nom unique pour éviter les doublons
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = path.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde de l'image", e);
        }
    }

    @Override
    public Acheteur modifierAcheteur(Long id, Acheteur acheteur) {
        Acheteur existant = getAcheteurById(id);
        existant.setNom(acheteur.getNom());
        existant.setEmail(acheteur.getEmail());
        existant.setLocalisation(acheteur.getLocalisation());
        // Ajoute les autres champs nécessaires
        return acheteurRepository.save(existant);
    }

    @Override
    public void supprimerAcheteur(Long id) {
        if (!acheteurRepository.existsById(id)) {
            throw new RuntimeException("Acheteur non trouvé");
        }
        acheteurRepository.deleteById(id);
    }

    @Override
    public Acheteur getAcheteurById(Long id) {
        return acheteurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Acheteur non trouvé avec l'ID : " + id));
    }

    @Override
    public List<Acheteur> getTousLesAcheteurs() {
        return List.of();
    }

    @Override
    public List<Acheteur> getTousLes_Acheteurs() {
        return acheteurRepository.findAll();
    }
}