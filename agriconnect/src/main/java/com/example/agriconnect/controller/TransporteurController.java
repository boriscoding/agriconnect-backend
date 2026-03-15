package com.example.agriconnect.controller;
import com.example.agriconnect.classes.Transporteur;
import com.example.agriconnect.service.TransporteurService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transporteurs")
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
public class TransporteurController {

    private final TransporteurService transporteurService;

    public TransporteurController(TransporteurService transporteurService) {
        this.transporteurService = transporteurService;
    }

    @PostMapping
    public Transporteur create(@RequestBody Transporteur transporteur) {
        return transporteurService.create(transporteur);
    }
    @PostMapping("/login")
    public Transporteur login(@RequestBody Transporteur credentials) {
        return transporteurService.findByEmailAndPassword(credentials.getEmail(), credentials.getPassword());
    }
    @GetMapping
    public List<Transporteur> getAll() {
        return transporteurService.findAll();
    }

    @GetMapping("/{id}")
    public Transporteur getById(@PathVariable Long id) {
        return transporteurService.findById(id);
    }

    @PutMapping("/{id}")
    public Transporteur update(
            @PathVariable Long id,
            @RequestBody Transporteur transporteur) {
        return transporteurService.update(id, transporteur);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        transporteurService.delete(id);
    }
}
