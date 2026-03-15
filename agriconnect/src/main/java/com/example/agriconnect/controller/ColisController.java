package com.example.agriconnect.controller;

import com.example.agriconnect.classes.Colis;
import com.example.agriconnect.service.ColisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
                "http://10.101.75.196:4200/"
        },
        allowCredentials = "true"
)
public class ColisController {
    private final ColisService colisService;

    @Autowired
    public ColisController(ColisService colisService) {
        this.colisService = colisService;
    }

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
}
