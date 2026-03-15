package com.example.agriconnect.service;

import com.example.agriconnect.classes.Producteur;
import com.example.agriconnect.classes.Transporteur;
import com.example.agriconnect.classes.Utilisateur;
import com.example.agriconnect.repository.UtilisateurRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;
    // Dans ton UtilisateurService.java
    @Override
    public Optional<Utilisateur> findById(Long id) {
        return utilisateurRepository.findById(id);
    }
    @Override
    public Optional<Utilisateur> getProfil(Long id) {
        return Optional.empty();
    }

    @Override
    public Utilisateur modifierProfil(Long id, Utilisateur userDetails) {
        return null;
    }

    @Override
    @Transactional
    public Utilisateur modifierProfil(Long id, Map<String, Object> updates) {
        // Log pour vérifier ce que le serveur reçoit vraiment
        System.out.println("Tentative de modification pour l'ID : " + id);

        return utilisateurRepository.findById(id).map(user -> {
            // Mise à jour des champs communs
            if (updates.get("nom") != null) user.setNom((String) updates.get("nom"));
            if (updates.get("localisation") != null) user.setLocalisation((String) updates.get("localisation"));
            if (updates.get("sexe") != null) user.setSexe((String) updates.get("sexe"));

            // Cast sécurisé pour le numéro (Integer ou Long selon ta classe)
            if (updates.get("number") != null) {
                user.setNumber(Integer.valueOf(updates.get("number").toString()));
            }

            // --- LOGIQUE SPECIFIQUE PAR ROLE ---

            // PRODUCTEUR
            if (user instanceof Producteur p) {
                if (updates.get("typeProduit") != null) p.setTypeProduit((String) updates.get("typeProduit"));
                if (updates.get("surfaceExploitation") != null) {
                    p.setSurfaceExploitation(Double.valueOf(updates.get("surfaceExploitation").toString()));
                }
            }

            // TRANSPORTEUR
            if (user instanceof Transporteur t) {
                if (updates.get("typeVehicule") != null) t.setTypeVehicule((String) updates.get("typeVehicule"));
                if (updates.get("capaciteCharge") != null) {
                    t.setCapaciteMax(Double.valueOf(updates.get("capaciteCharge").toString()));
                }
            }

            return utilisateurRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'ID : " + id));
    }
}
