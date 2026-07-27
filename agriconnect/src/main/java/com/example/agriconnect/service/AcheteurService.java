package com.example.agriconnect.service;

import com.example.agriconnect.classes.Acheteur;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface AcheteurService {
    Acheteur creerAcheteur(Acheteur acheteur, MultipartFile file);

    Acheteur modifierAcheteur(Long id, Acheteur acheteur);

    void supprimerAcheteur(Long id);

    Acheteur getAcheteurById(Long id);

    List<Acheteur> getTousLesAcheteurs();
    Acheteur findByEmailAndPassword(String email, String password);
    public Acheteur modifierProfil(Long id, Map<String, Object> updates, MultipartFile file);
    public List<Acheteur> getTousLes_Acheteurs();
}
