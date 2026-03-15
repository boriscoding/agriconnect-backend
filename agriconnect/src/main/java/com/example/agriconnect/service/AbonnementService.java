package com.example.agriconnect.service;
import com.example.agriconnect.classes.Abonnement;

import java.util.List;

public interface AbonnementService  {
    Abonnement creerAbonnement(Abonnement abonnement);

    Abonnement modifierAbonnement(Long id, Abonnement abonnement);

    void supprimerAbonnement(Long id);

    Abonnement getAbonnementById(Long id);

    List<Abonnement> getTousLesAbonnements();

    Abonnement getAbonnementParUtilisateur(Long utilisateurId);
}
