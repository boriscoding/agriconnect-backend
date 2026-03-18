package com.example.agriconnect.controller;

import com.example.agriconnect.classes.Vehicule;
import com.example.agriconnect.service.VehiculeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicules")
@CrossOrigin(
        origins = {
                "http://localhost:4200",
                "http://172.27.208.1:4200",
                "http://192.168.56.1:4200",
                "http://192.168.197.1:4200",
                "http://10.177.225.196:4200",
                "http://10.177.225.196:4200/",
                "http://10.101.75.196:4200/"
        },
        allowCredentials = "true"
)
public class VehiculeController {

    @Autowired
    private VehiculeService vehiculeService;

    private final String UPLOAD_DIR = "uploads/";


    // --- 1. ENREGISTRER UN VÉHICULE ---
    @PostMapping("/enregistrer")
    public ResponseEntity<Vehicule> enregistrer(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("marque") String marque,
            @RequestParam("model") String model,
            @RequestParam("immatriculation") String immatriculation,
            @RequestParam("voitureType") String voitureType,
            @RequestParam("description") String description,
            @RequestParam("chargeMax") Double chargeMax,
            @RequestParam("transporteurId") Long transporteurId) {

        try {
            Vehicule vehicule = new Vehicule();
            vehicule.setMarque(marque);
            vehicule.setModel(model);
            vehicule.setImmatriculation(immatriculation);
            vehicule.setVoitureType(voitureType);
            vehicule.setDescription(description);
            vehicule.setChargeMax(chargeMax);

            if (file != null && !file.isEmpty()) {
                File directory = new File(UPLOAD_DIR);
                if (!directory.exists()) directory.mkdirs();

                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR + fileName);
                Files.write(path, file.getBytes());

                // ✅ ASTUCE : Ne stocke que le NOM du fichier, pas l'URL complète avec localhost.
                // C'est le Frontend qui ajoutera l'IP. Sinon, ça ne marchera pas sur téléphone.
                vehicule.setPhoto(fileName);
                vehicule.setMediaUrl(fileName);
            }

            Vehicule nouveauVehicule = vehiculeService.enregistrerVehiculeComplet(vehicule, transporteurId);
            return new ResponseEntity<>(nouveauVehicule, HttpStatus.CREATED);

        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Route conservée pour la compatibilité
    @GetMapping("/liste")
    public ResponseEntity<List<Vehicule>> listerTous() {
        return ResponseEntity.ok(vehiculeService.findAll()); // Utilise directement le service
    }

    // --- 3. TROUVER UN VÉHICULE PAR SON ID ---
    @GetMapping("/trouver/{id}")
    public ResponseEntity<Vehicule> trouverParId(@PathVariable("id") Long id) {
        Vehicule vehicule = vehiculeService.trouverParId(id);
        if (vehicule != null) {
            return new ResponseEntity<>(vehicule, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --- 4. SUPPRIMER UN VÉHICULE ---
    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<HttpStatus> supprimer(@PathVariable("id") Long id) {
        try {
            vehiculeService.supprimerVehicule(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // --- 5. LISTER PAR TRANSPORTEUR ---
    @GetMapping("/transporteur/{id}")
    public ResponseEntity<List<Vehicule>> listerParTransporteur(@PathVariable("id") Long id) {
        List<Vehicule> vehicules = vehiculeService.trouverParTransporteur(id);
        return new ResponseEntity<>(vehicules, HttpStatus.OK);
    }
    @PutMapping("/modifier/{id}")
    public ResponseEntity<Vehicule> modifier(
            @PathVariable Long id,
            @RequestBody Vehicule vehiculeDetails
    ) {
        Vehicule updatedVehicule = vehiculeService.modifierVehicule(id, vehiculeDetails);
        return ResponseEntity.ok(updatedVehicule);
    }
    @GetMapping("/toutes")
    public List<Vehicule> getAllVehicules() {
        return vehiculeService.findAll();
    }
}