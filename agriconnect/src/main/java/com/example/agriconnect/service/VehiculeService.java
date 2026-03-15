package com.example.agriconnect.service;

import com.example.agriconnect.classes.Vehicule;
import java.util.List;

public interface VehiculeService {
    Vehicule enregistrerVehicule(Vehicule vehicule);
    List<Vehicule> recupererTousLesVehicules();
    Vehicule trouverParId(Long id);
    void supprimerVehicule(Long id);
    List<Vehicule> trouverParTransporteur(Long transporteurId);
    // Ajoute cette signature de méthode
    Vehicule enregistrerVehiculeComplet(Vehicule vehicule, Long transporteurId);
}