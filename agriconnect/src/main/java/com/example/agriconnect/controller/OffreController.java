package com.example.agriconnect.controller;

import com.example.agriconnect.classes.Offre;
import com.example.agriconnect.service.OffreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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
                "http://10.177.225.196:4200/",
                "http://10.101.75.196:4200/"
        },
        allowCredentials = "true"
)
@RequiredArgsConstructor
public class OffreController {

    private final OffreService service;

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

        // 1. On crée l'objet et on remplit les champs texte
        Offre offre = new Offre();
        offre.setTitre(titre);
        offre.setNproduit(nomProduit);
        offre.setType_pro(categorie);
        offre.setDescription(description);
        offre.setPrixUnitaire(prixUnitaire);
        offre.setQuantiteProduit(quantiteProduit);
        offre.setLieuProduction(lieuProduction);
        offre.setStatut(statut);
        offre.setDatePublication(LocalDate.now());

        // 2. ON LAISSE LE SERVICE TOUT FAIRE (Gestion image + liaison producteur + Save)
        // Le service s'occupera du UUID et de Files.write
        Offre savedOffre = service.enregistrerDirectement(offre, producteurId, file);

        return ResponseEntity.ok(savedOffre);
    }

    @GetMapping("/toutes")
    public List<Offre> getAllOffres() {
        return service.recupererToutesLesOffres();
    }
    // --- SUPPRIMER UNE OFFRE ---
    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        service.supprimerOffre(id);
        return ResponseEntity.ok().build();
    }

    // --- MODIFIER UNE OFFRE ---
    @PutMapping("/modifier/{id}")
    public ResponseEntity<Offre> modifier(
            @PathVariable Long id,
            @RequestBody Offre offreDetails
    ) {
        // On passe l'ID et l'objet contenant les nouvelles infos au service
        Offre updatedOffre = service.modifierOffre(id, offreDetails);
        return ResponseEntity.ok(updatedOffre);
    }
}