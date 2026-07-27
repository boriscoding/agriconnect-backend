package com.example.agriconnect.controller;

import com.example.agriconnect.classes.Offre;
import com.example.agriconnect.repository.OffreRepository; // AJOUT : Import du repository
import com.example.agriconnect.service.OffreService;
import com.example.agriconnect.dto.CommandeRequest; // AJOUT : Crée cette classe DTO
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; // AJOUT : Import transaction
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/offres")
@CrossOrigin(
        origins = {
                "http://localhost:4200",
                "http://172.27.208.1:4200",
                "http://192.168.56.1:4200",
                "http://192.168.197.1:4200",
                "http://192.168.226.1:4200",
                "http://10.177.225.196:4200",
                "http://10.101.75.196:4200",
                "https://unsacked-improvisationally-suanne.ngrok-free.dev",
                "https://agrilinkbycam.netlify.app/"

        },
        allowCredentials = "true"
)
@RequiredArgsConstructor
public class OffreController {

    private final OffreService service;
    private final OffreRepository offreRepository; // AJOUT : Injection nécessaire pour la méthode commander

    @PostMapping("/publier")
    public ResponseEntity<Offre> creer(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("titre") String titre,
            @RequestParam("nomProduit") String nomProduit,
            @RequestParam("categorie") String categorie,
            @RequestParam("description") String description,
            @RequestParam("prixUnitaire") Double prixUnitaire,
            @RequestParam("quantiteProduit") Double quantiteProduit,
            @RequestParam("lieuProduction") String lieuProduction,
            @RequestParam("statut") String statut,
            @RequestParam("producteurId") Long producteurId
    ) throws IOException {

        Offre offre = new Offre();
        offre.setTitre(titre);
        offre.setNproduit(nomProduit);
        offre.setType_pro(categorie);
        offre.setDescription(description);
        offre.setPrixUnitaire(prixUnitaire);
        offre.setQuantiteProduit(quantiteProduit);
        // Initialiser la quantité restante à la quantité totale lors de la création
        offre.setQuantiteRestante(quantiteProduit);
        offre.setLieuProduction(lieuProduction);
        offre.setStatut(statut);
        offre.setDatePublication(LocalDate.now());

        Offre savedOffre = service.enregistrerDirectement(offre, producteurId, file);
        return ResponseEntity.ok(savedOffre);
    }

    @GetMapping("/toutes")
    public List<Offre> getAllOffres() {
        return service.recupererToutesLesOffres();
    }

    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        service.supprimerOffre(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/modifier/{id}")
    public ResponseEntity<Offre> modifier(@PathVariable Long id, @RequestBody Offre offreDetails) {
        Offre updatedOffre = service.modifierOffre(id, offreDetails);
        return ResponseEntity.ok(updatedOffre);
    }

    @PostMapping("/commander")
    @Transactional // Pour s'assurer que si la mise à jour échoue, rien n'est déduit
    public ResponseEntity<?> finaliserCommande(@RequestBody CommandeRequest request) {
        try {
            // 1. Récupérer l'offre via le repository injecté
            Offre offre = offreRepository.findById(request.getOffreId())
                    .orElseThrow(() -> new RuntimeException("Offre non trouvée"));

            // 2. Vérification du stock
            if (offre.getQuantiteRestante() == null) {
                offre.setQuantiteRestante(offre.getQuantiteProduit());
            }

            if (offre.getQuantiteRestante() < request.getQuantite()) {
                return ResponseEntity.badRequest().body("Stock insuffisant. Disponible: " + offre.getQuantiteRestante());
            }

            // 3. Mise à jour de la quantité restante
            offre.setQuantiteRestante(offre.getQuantiteRestante() - request.getQuantite());
            offreRepository.save(offre);

            return ResponseEntity.ok().body("{\"message\": \"Commande validée avec succès\"}");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur serveur : " + e.getMessage());
        }
    }
}