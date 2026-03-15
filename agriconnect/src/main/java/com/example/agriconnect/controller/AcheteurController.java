package com.example.agriconnect.controller;

import com.example.agriconnect.classes.Acheteur;
import com.example.agriconnect.repository.AcheteurRepository;
import com.example.agriconnect.service.AcheteurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/acheteurs")
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
public class AcheteurController {

    private final AcheteurService acheteurService;

    @PostMapping("/login")
    public Acheteur login(@RequestBody Acheteur credentials) {
        return acheteurService.findByEmailAndPassword(credentials.getEmail(), credentials.getPassword());
    }

    // CREATE
    @Autowired
    public AcheteurController(AcheteurService acheteurService) {
        this.acheteurService = acheteurService;
    }

    // --- CORRECTION ICI ---
    @PostMapping
    public Acheteur creer(@RequestBody Acheteur acheteur) {
        // Cette méthode appelle ton service qui, lui, appelle le repository.save()
        return acheteurService.creerAcheteur(acheteur);
    }
    // -----------------------

    @GetMapping
    public List<Acheteur> getAll() {
        return acheteurService.getTousLesAcheteurs();
    }

    // READ ONE
    @GetMapping("/{id}")
    public Acheteur getById(@PathVariable Long id) {
        return acheteurService.getAcheteurById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Acheteur modifier(@PathVariable Long id, @RequestBody Acheteur acheteur) {
        return acheteurService.modifierAcheteur(id, acheteur);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void supprimer(@PathVariable Long id) {
        acheteurService.supprimerAcheteur(id);
    }



}
