package com.example.agriconnect.service;

import com.example.agriconnect.classes.Paiement;
import java.util.Map;

public interface PaiementService {
    // Initialise la transaction et retourne l'URL de paiement
    String initialiserPaiement(Double montant, String mode, Long acheteurId, Long demandeId);

    // Vérifie si le paiement a été réellement effectué (Webhook ou Check)
    boolean verifierStatut(String reference);
}