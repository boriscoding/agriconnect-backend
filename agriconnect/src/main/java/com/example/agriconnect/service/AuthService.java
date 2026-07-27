package com.example.agriconnect.service;

import com.example.agriconnect.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UtilisateurRepository utilisateurRepository; // Doit être déclaré

    public AuthService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository; // Injecté ici
    }
}