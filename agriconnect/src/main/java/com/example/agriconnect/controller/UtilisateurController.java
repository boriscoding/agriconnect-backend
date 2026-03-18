package com.example.agriconnect.controller;

import com.example.agriconnect.classes.Utilisateur;
import com.example.agriconnect.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/utilisateurs")
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
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService; // Utilise le Service au lieu du Repository

    // Récupérer le profil (Spring renverra l'objet complet : Producteur, Transporteur ou Acheteur)
    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getUtilisateur(@PathVariable Long id) {
        System.out.println("Requête reçue pour l'ID : " + id); // <--- AJOUTE CECI
        return utilisateurService.findById(id)
                .map(user -> {
                    System.out.println("Utilisateur trouvé : " + user.getNom());
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> {
                    System.out.println("Utilisateur " + id + " NON TROUVÉ en base.");
                    return ResponseEntity.notFound().build();
                });
    }


    // UNE SEULE méthode de mise à jour utilisant une Map pour la flexibilité
    @PutMapping(value = "/modifier/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Utilisateur> modifierProfil(
            @PathVariable Long id,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestParam("nom") String nom,
            @RequestParam("localisation") String localisation,
            @RequestParam("sexe") String sexe,
            @RequestParam("number") Integer number
    ) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("nom", nom);
        updates.put("localisation", localisation);
        updates.put("sexe", sexe);
        updates.put("number", number);

        // Appel au service avec gestion d'exception pour le fichier
        Utilisateur updatedUser = utilisateurService.modifierProfilAvecImage(id, updates, file);
        return ResponseEntity.ok(updatedUser);
    }
}