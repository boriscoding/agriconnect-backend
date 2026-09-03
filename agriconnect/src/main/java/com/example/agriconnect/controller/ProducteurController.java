package com.example.agriconnect.controller;

import com.example.agriconnect.classes.Producteur;
import com.example.agriconnect.service.ProducteurService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/producteurs")
@CrossOrigin(
        origins = {
                "http://localhost:4200",
                "http://172.27.208.1:4200",
                "http://192.168.56.1:4200",
                "http://192.168.197.1:4200",
                "http://192.168.226.1:4200",
                "http://10.177.225.196:4200",
                "https://unsacked-improvisationally-suanne.ngrok-free.dev",
                "https://agrilinkbycam.netlify.app"

        },
        allowCredentials = "true"
)
public class ProducteurController {

    private final ProducteurService producteurService;

    public ProducteurController(ProducteurService producteurService) {
        this.producteurService = producteurService;
    }

    // --- AUTHENTIFICATION ---
    // ⚠️ Corrigé : renvoyait un 200 avec un corps vide en cas d'échec, ce qui
    // faisait afficher côté Angular un message trompeur ("données incomplètes"
    // au lieu de "email ou mot de passe incorrect"). Maintenant un vrai 401,
    // comme AcheteurController le fait déjà.
    @PostMapping("/login")
    public ResponseEntity<Producteur> login(@RequestBody Producteur credentials) {
        Producteur producteur = producteurService.findByEmailAndPassword(
                credentials.getEmail(), credentials.getPassword());
        return (producteur != null) ? ResponseEntity.ok(producteur) : ResponseEntity.status(401).build();
    }

    // --- CRUD DE BASE ---

    // ✅ CORRECTION : Utilisation de @ModelAttribute et spécification du type de contenu
    // Cela permet de recevoir les données envoyées via FormData par Angular
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Producteur create(@ModelAttribute Producteur producteur) {
        return producteurService.create(producteur);
    }

    @GetMapping
    public List<Producteur> getAll() {
        return producteurService.findAll();
    }

    @GetMapping("/{id}")
    public Producteur getById(@PathVariable Long id) {
        return producteurService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        producteurService.delete(id);
    }

    // --- MISE À JOUR DU PROFIL (Avec Image) ---
    @PutMapping(value = "/modifier/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Producteur> updateProducteur(
            @PathVariable Long id,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestParam("nom") String nom,
            @RequestParam("email") String email,
            @RequestParam("localisation") String localisation,
            @RequestParam("sexe") String sexe,
            @RequestParam("number") Integer number,
            @RequestParam(value = "typeProduit", required = false) String typeProduit,
            @RequestParam(value = "surfaceExploitation", required = false) Double surfaceExploitation
    ) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("nom", nom);
        updates.put("email", email);
        updates.put("localisation", localisation);
        updates.put("sexe", sexe);
        updates.put("number", number);
        updates.put("typeProduit", typeProduit);
        updates.put("surfaceExploitation", surfaceExploitation);

        Producteur updated = producteurService.modifierProfilAvecImage(id, updates, file);
        return ResponseEntity.ok(updated);
    }
}