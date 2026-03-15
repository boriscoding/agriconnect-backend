package com.example.agriconnect.service;

import com.example.agriconnect.classes.Acheteur;

import java.util.List;

public interface AcheteurService {
    Acheteur creerAcheteur(Acheteur acheteur);

    Acheteur modifierAcheteur(Long id, Acheteur acheteur);

    void supprimerAcheteur(Long id);

    Acheteur getAcheteurById(Long id);

    List<Acheteur> getTousLesAcheteurs();
    Acheteur findByEmailAndPassword(String email, String password);
}
