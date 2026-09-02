package com.example.agriconnect.controller;

import com.example.agriconnect.classes.Acheteur;
import com.example.agriconnect.classes.DemandesAcheteur;
import com.example.agriconnect.classes.Offre;
import com.example.agriconnect.repository.AcheteurRepository;
import com.example.agriconnect.repository.DemandesAcheteurRepository;
import com.example.agriconnect.repository.OffreRepository;
import com.example.agriconnect.service.DemandesAcheteurService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/demandes")
@CrossOrigin(
        origins = {
                "http://localhost:4200",
                "http://172.27.208.1:4200",
                "http://192.168.56.1:4200",
                "http://192.168.197.1:4200",
                "http://192.168.226.1:4200",
                "http://10.177.225.196:4200",
                "http://10.101.75.196:4200"  ,
                "https://unsacked-improvisationally-suanne.ngrok-free.dev",
                "https://agrilinkbycam.netlify.app"

        },
        allowCredentials = "true"
)
@RequiredArgsConstructor
public class DemandesAcheteurController {

    private final DemandesAcheteurService service;

    // Déclarations des outils (Repositories)
    private final AcheteurRepository acheteurRepository;
    private final OffreRepository offreRepository;
    private final DemandesAcheteurRepository demandeRepo; // <--- C'est lui qu'on va utiliser pour sauvegarder

    /**
     * CRÉER via JSON
     */
    @PostMapping
    public ResponseEntity<DemandesAcheteur> create(@RequestBody DemandesAcheteur demande) {
        return new ResponseEntity<>(service.create(demande), HttpStatus.CREATED);
    }

    /**
     * CRÉER via Paramètres
     * Changement : Ajout de @PostMapping("/creer") et utilisation de demandeRepo
     */
    @PostMapping("/creer")
    @Transactional
    public ResponseEntity<DemandesAcheteur> creerDemandeDepuisParams(
            @RequestParam String libelle,
            @RequestParam double quantite,
            @RequestParam Long acheteurId,
            @RequestParam Long offreId) {

        Acheteur acheteur = acheteurRepository.findById(acheteurId)
                .orElseThrow(() -> new RuntimeException("Acheteur non trouvé"));

        Offre offre = offreRepository.findById(offreId)
                .orElseThrow(() -> new RuntimeException("Offre non trouvée"));

        DemandesAcheteur demande = new DemandesAcheteur();
        demande.setLibeller(libelle);
        demande.setQuantite_c(quantite);
        demande.setAcheteur(acheteur);
        demande.setOffre(offre);
        demande.setStatut("EN_ATTENTE");

        // CORRECTION ICI : Utilise 'demandeRepo' au lieu de 'repository'
        DemandesAcheteur sauvee = demandeRepo.save(demande);
        return ResponseEntity.ok(sauvee);
    }

    /**
     * RÉCUPÉRER TOUT
     */
    @GetMapping("/toutes")
    public List<DemandesAcheteur> getAll() {
        return service.findAll();
    }

    /**
     * RÉCUPÉRER PAR ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<DemandesAcheteur> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    /**
     * RÉCUPÉRER TOUTES LES DEMANDES D'UN ACHETEUR
     */
    @GetMapping("/acheteur/{acheteurId}")
    public List<DemandesAcheteur> getByAcheteur(@PathVariable Long acheteurId) {
        return service.findByAcheteurId(acheteurId);
    }

    /**
     * METTRE À JOUR
     */
    @PutMapping("/{id}")
    public ResponseEntity<DemandesAcheteur> update(@PathVariable Long id, @RequestBody DemandesAcheteur demande) {
        return ResponseEntity.ok(service.update(id, demande));
    }

    /**
     * SUPPRIMER
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}