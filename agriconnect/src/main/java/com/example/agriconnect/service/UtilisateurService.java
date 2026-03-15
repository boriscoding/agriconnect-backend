package com.example.agriconnect.service;
import com.example.agriconnect.classes.Utilisateur;

import java.util.Map;
import java.util.Optional;

public interface UtilisateurService {
    Optional<Utilisateur> getProfil(Long id);
    Utilisateur modifierProfil(Long id, Utilisateur userDetails);
    public Utilisateur modifierProfil(Long id, Map<String, Object> updates);
    public Optional<Utilisateur> findById(Long id);
}
