package com.example.agriconnect.controller;

import com.example.agriconnect.classes.PositionGPS;
import com.example.agriconnect.service.PositionGPSService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/positions")
@CrossOrigin(
        origins = {
                "http://localhost:4200",
                "http://172.27.208.1:4200",
                "http://192.168.56.1:4200",
                "http://192.168.197.1:4200",
                "http://192.168.226.1:4200",
                "http://10.177.225.196:4200",
                "http://10.177.225.196:4200/",
                "http://10.101.75.196:4200/",
                "https://unsacked-improvisationally-suanne.ngrok-free.dev",
                "https://agrilinkbycam.netlify.app"


        },
        allowCredentials = "true"
)
public class PositionGPSController {
    private final PositionGPSService service;

    public PositionGPSController(PositionGPSService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public PositionGPS creer(@RequestBody PositionGPS positionGPS) {
        return service.creer(positionGPS);
    }

    // READ ALL
    @GetMapping
    public List<PositionGPS> toutes() {
        return service.getToutes();
    }

    // READ ONE
    @GetMapping("/{id}")
    public PositionGPS une(@PathVariable Long id) {
        return service.getById(id);
    }

    // READ BY TRANSPORTEUR
    @GetMapping("/transporteur/{id}")
    public List<PositionGPS> parTransporteur(@PathVariable Long id) {
        return service.getParTransporteur(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public PositionGPS modifier(
            @PathVariable Long id,
            @RequestBody PositionGPS positionGPS) {
        return service.modifier(id, positionGPS);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void supprimer(@PathVariable Long id) {
        service.supprimer(id);
    }
}
