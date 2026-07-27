package com.example.agriconnect.controller;

import com.example.agriconnect.classes.Utilisateur;
import com.example.agriconnect.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/auth")
// Remplace "*" par l'URL précise pour éviter le retour de l'erreur précédente
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
public class AuthController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean exists = utilisateurRepository.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    // --- MÉTHODE D'INSCRIPTION ---
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Utilisateur newUser) {
        // 1. Vérifier si l'email existe déjà
        if (utilisateurRepository.existsByEmail(newUser.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Erreur : Cet email est déjà utilisé !");
        }

        // 2. Sauvegarder l'utilisateur
        // Note: En production, il faudra crypter le mot de passe ici
        Utilisateur savedUser = utilisateurRepository.save(newUser);

        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        return utilisateurRepository.findByEmail(credentials.get("email"))
                .map(user -> {
                    if (user.getPassword().equals(credentials.get("password"))) {
                        return ResponseEntity.ok(user);
                    }
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}