package com.example.agriconnect.controller;

import com.example.agriconnect.classes.Abonnement;
import com.example.agriconnect.service.AbonnementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/abonnements")
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
        allowCredentials = "true"
)
public class AbonnementController {
    private final AbonnementService abonnementService;

    public AbonnementController(AbonnementService abonnementService) {
        this.abonnementService = abonnementService;
    }

    // CREATE
    @PostMapping
    public Abonnement creer(@RequestBody Abonnement abonnement) {
        return abonnementService.creerAbonnement(abonnement);
    }

    // READ ALL
    @GetMapping
    public List<Abonnement> getAll() {
        return abonnementService.getTousLesAbonnements();
    }

    // READ ONE
    @GetMapping("/{id}")
    public Abonnement getById(@PathVariable Long id) {
        return abonnementService.getAbonnementById(id);
    }

    // READ BY UTILISATEUR
    @GetMapping("/utilisateur/{utilisateurId}")
    public Abonnement getByUtilisateur(@PathVariable Long utilisateurId) {
        return abonnementService.getAbonnementParUtilisateur(utilisateurId);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Abonnement modifier(@PathVariable Long id, @RequestBody Abonnement abonnement) {
        return abonnementService.modifierAbonnement(id, abonnement);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void supprimer(@PathVariable Long id) {
        abonnementService.supprimerAbonnement(id);
    }
}
