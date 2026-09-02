package com.example.agriconnect.controller;

import com.example.agriconnect.classes.Transporteur;
import com.example.agriconnect.service.TransporteurService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transporteurs")
@CrossOrigin(
        origins = {
                "http://localhost:4200",
                "http://172.27.208.1:4200",
                "http://10.177.225.196:4200",
                "https://unsacked-improvisationally-suanne.ngrok-free.dev",
                "https://agrilinkbycam.netlify.app"

        },
        allowCredentials = "true"
)
public class TransporteurController {

    private final TransporteurService transporteurService;

    public TransporteurController(TransporteurService transporteurService) {
        this.transporteurService = transporteurService;
    }

    // --- INSCRIPTION (Correction @ModelAttribute pour FormData) ---
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Transporteur create(@ModelAttribute Transporteur transporteur) {
        return transporteurService.create(transporteur);
    }

    // --- CONNEXION ---
    @PostMapping("/login")
    public Transporteur login(@RequestBody Transporteur credentials) {
        return transporteurService.findByEmailAndPassword(credentials.getEmail(), credentials.getPassword());
    }

    // --- MISE À JOUR DU PROFIL (Avec Image) ---
    @PutMapping(value = "/modifier/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Transporteur> updateTransporteur(
            @PathVariable Long id,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestParam("nom") String nom,
            @RequestParam("email") String email,
            @RequestParam("localisation") String localisation,
            @RequestParam("sexe") String sexe,
            @RequestParam("number") Integer number,
            @RequestParam(value = "typeVehicule", required = false) String typeVehicule,
            @RequestParam(value = "capaciteMax", required = false) Double capaciteMax
    ) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("nom", nom);
        updates.put("email", email);
        updates.put("localisation", localisation);
        updates.put("sexe", sexe);
        updates.put("number", number);
        updates.put("typeVehicule", typeVehicule);
        updates.put("capaciteMax", capaciteMax);

        Transporteur updated = transporteurService.modifierProfilAvecImage(id, updates, file);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public List<Transporteur> getAll() {
        return transporteurService.findAll();
    }

    @GetMapping("/{id}")
    public Transporteur getById(@PathVariable Long id) {
        return transporteurService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        transporteurService.delete(id);
    }
}