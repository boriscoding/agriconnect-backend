package com.example.agriconnect.controller;

import com.example.agriconnect.classes.Producteur;
import com.example.agriconnect.repository.ProducteurRepository;
import com.example.agriconnect.service.ProducteurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/producteurs")
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
public class ProducteurController {
    private final ProducteurService producteurService;

    public ProducteurController(ProducteurService producteurService) {
        this.producteurService = producteurService;
    }
    // --- NOUVELLE MÉTHODE DE CONNEXION ---
    @PostMapping("/login")
    public Producteur login(@RequestBody Producteur credentials) {
        // On cherche le producteur par email et password via le service
        return producteurService.findByEmailAndPassword(credentials.getEmail(), credentials.getPassword());
    }
    // CREATE (Inscription)
    @PostMapping
    public Producteur create(@RequestBody Producteur producteur) {

        return producteurService.create(producteur);
    }

    // READ ALL
    @GetMapping
    public List<Producteur> getAll() {
        return producteurService.findAll();
    }

    // READ ONE
    @GetMapping("/{id}")
    public Producteur getById(@PathVariable Long id) {
        return producteurService.findById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Producteur update(
            @PathVariable Long id,
            @RequestBody Producteur producteur) {
        return producteurService.update(id, producteur);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        producteurService.delete(id);
    }
}
