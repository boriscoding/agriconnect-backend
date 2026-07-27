package com.example.agriconnect.controller;

import com.example.agriconnect.classes.Acheteur;
import com.example.agriconnect.service.AcheteurService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/acheteurs")
@CrossOrigin(
        origins = {
                "http://localhost:4200",
                "http://172.27.208.1:4200",
                "http://10.177.225.196:4200" ,
                "https://unsacked-improvisationally-suanne.ngrok-free.dev",
                "https://agrilinkbycam.netlify.app/"

        },
        allowCredentials = "true"
)
public class AcheteurController {

    private final AcheteurService acheteurService;

    public AcheteurController(AcheteurService acheteurService) {
        this.acheteurService = acheteurService;
    }

    // --- INSCRIPTION (Simplifiée via @ModelAttribute) ---
    @PostMapping(value = "/inscription", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Acheteur> inscription(
            @ModelAttribute Acheteur acheteur,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        // Le mappage se fait automatiquement entre FormData et l'objet Acheteur
        return ResponseEntity.ok(acheteurService.creerAcheteur(acheteur, file));
    }

    // --- CONNEXION ---
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Acheteur> login(@RequestBody Acheteur credentials) {
        Acheteur acheteur = acheteurService.findByEmailAndPassword(
                credentials.getEmail(), credentials.getPassword());
        if (acheteur != null) {
            return ResponseEntity.ok(acheteur);
        }
        return ResponseEntity.status(401).build();
    }

    // --- MISE À JOUR DU PROFIL ---
    @PutMapping(value = "/modifier/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Acheteur> updateAcheteur(
            @PathVariable Long id,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestParam("nom") String nom,
            @RequestParam("email") String email,
            @RequestParam("localisation") String localisation,
            @RequestParam("number") Integer number,
            @RequestParam("sexe") String sexe,
            @RequestParam(value = "adresseLivraison", required = false) String adresseLivraison
    ) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("nom", nom);
        updates.put("email", email);
        updates.put("localisation", localisation);
        updates.put("number", number);
        updates.put("sexe", sexe);
        updates.put("adresseLivraison", adresseLivraison);

        Acheteur updated = acheteurService.modifierProfil(id, updates, file);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public List<Acheteur> getAll() {
        return acheteurService.getTousLesAcheteurs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Acheteur> getById(@PathVariable Long id) {
        Acheteur acheteur = acheteurService.getAcheteurById(id);
        return (acheteur != null) ? ResponseEntity.ok(acheteur) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public void supprimer(@PathVariable Long id) {
        acheteurService.supprimerAcheteur(id);
    }
}