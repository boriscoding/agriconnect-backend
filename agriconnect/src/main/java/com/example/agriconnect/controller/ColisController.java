package com.example.agriconnect.controller;

import com.example.agriconnect.classes.Colis;
import com.example.agriconnect.dto.CommandeOffreDTO;
import com.example.agriconnect.service.ColisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// ═══════════════════════════════════════════════════════════════
// POST /api/colis/commander  ←  Point d'entrée unique de la commande
//
// Ce contrôleur délègue TOUT au ColisService.traiterCommandeOffre()
// qui doit :
//   1. Vérifier le stock de l'offre
//   2. Décrémenter quantiteRestante dans l'offre
//   3. Créer une DemandeAcheteur
//   4. Créer ou récupérer le Colis (selon modeColis)
//   5. Associer le colis à la Transaction de transport
// ═══════════════════════════════════════════════════════════════

@RestController
@RequestMapping("/api/colis")
@CrossOrigin(
        origins = {
                "http://localhost:4200",
                "http://172.27.208.1:4200",
                "http://192.168.56.1:4200",
                "http://192.168.197.1:4200",
                "http://192.168.226.1:4200",
                "http://10.177.225.196:4200",
                "http://10.177.225.196:4200/",
                "http://10.101.75.196:4200/" ,
                "https://unsacked-improvisationally-suanne.ngrok-free.dev",
                "https://agrilinkbycam.netlify.app/"

        },
        allowedHeaders = "*",
        allowCredentials = "true"
)
public class ColisController {

    private final ColisService colisService;

    @Autowired
    public ColisController(ColisService colisService) {
        this.colisService = colisService;
    }

    // ─── CRUD de base ────────────────────────────────────────
    @PostMapping
    public Colis createColis(@RequestBody Colis colis) {
        return colisService.saveColis(colis);
    }

    @GetMapping
    public List<Colis> getAllColis() {
        return colisService.getAllColis();
    }

    @GetMapping("/{id}")
    public Optional<Colis> getColisById(@PathVariable Long id) {
        return colisService.getColisById(id);
    }

    @PutMapping("/{id}")
    public Colis updateColis(@PathVariable Long id, @RequestBody Colis colis) {
        return colisService.updateColis(id, colis);
    }

    @DeleteMapping("/{id}")
    public void deleteColis(@PathVariable Long id) {
        colisService.deleteColis(id);
    }

    // ─── COMMANDE PRINCIPALE ──────────────────────────────────
    // Reçoit le payload d'Angular app-commande.ts → enregistrerCommande()
    @PostMapping("/commander")
    public ResponseEntity<?> commander(@RequestBody CommandeOffreDTO dto) {
        try {
            Colis result = colisService.traiterCommandeOffre(dto);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            // Erreur métier (stock insuffisant, offre introuvable, etc.)
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur serveur : " + e.getMessage());
        }
    }
}