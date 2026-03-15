package com.example.agriconnect.controller;

import com.example.agriconnect.classes.DemandesAcheteur;
import com.example.agriconnect.service.DemandesAcheteurService;
import lombok.RequiredArgsConstructor;
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
                "http://10.177.225.196:4200/",
                "http://10.101.75.196:4200/"
        },
        allowCredentials = "true"
)
@RequiredArgsConstructor
public class DemandesAcheteurController {

    private final DemandesAcheteurService service;

    @PostMapping
    public DemandesAcheteur create(@RequestBody DemandesAcheteur demande) {
        return service.create(demande);
    }

    @GetMapping
    public List<DemandesAcheteur> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public DemandesAcheteur getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/acheteur/{acheteurId}")
    public List<DemandesAcheteur> getByAcheteur(@PathVariable Long acheteurId) {
        return service.findByAcheteurId(acheteurId);
    }

    @PutMapping("/{id}")
    public DemandesAcheteur update(@PathVariable Long id, @RequestBody DemandesAcheteur demande) {
        return service.update(id, demande);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}