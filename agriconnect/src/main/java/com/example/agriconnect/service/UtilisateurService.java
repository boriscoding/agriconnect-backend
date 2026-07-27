package com.example.agriconnect.service;
import aj.org.objectweb.asm.commons.Remapper;
import com.example.agriconnect.classes.Utilisateur;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public interface UtilisateurService {
    Optional<Utilisateur> getProfil(Long id);
    Utilisateur modifierProfil(Long id, Utilisateur userDetails);
    public Utilisateur modifierProfil(Long id, Map<String, Object> updates);
    public Optional<Utilisateur> findById(Long id);
    public Utilisateur modifierProfilAvecImage(Long id, Map<String, Object> updates, MultipartFile file) ;

    public Optional<Utilisateur> findByEmail(String email);
}
