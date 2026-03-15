package com.example.agriconnect.service;

import com.example.agriconnect.classes.Transporteur;
import com.example.agriconnect.classes.Vehicule;
import com.example.agriconnect.repository.TransporteurRepository;
import com.example.agriconnect.repository.VehiculeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class VehiculeServiceImplement implements VehiculeService {

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Override
    public Vehicule enregistrerVehicule(Vehicule vehicule) {
        // La dateEnregistrement est gérée par @PrePersist dans l'entité
        // On peut ajouter ici une vérification sur la charge_max du diagramme
        if (vehicule.getChargeMax() != null && vehicule.getChargeMax() < 0) {
            throw new IllegalArgumentException("La charge maximale ne peut pas être négative");
        }
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public List<Vehicule> recupererTousLesVehicules() {
        return vehiculeRepository.findAll();
    }

    @Override
    public Vehicule trouverParId(Long id) {
        return vehiculeRepository.findById(id).orElse(null);
    }

    @Override
    public void supprimerVehicule(Long id) {
        vehiculeRepository.deleteById(id);
    }

    @Override
    public List<Vehicule> trouverParTransporteur(Long transporteurId) {
        // Cette méthode nécessite d'être ajoutée dans le Repository d'abord
        return vehiculeRepository.findAll();
    }
    // N'oublie pas d'injecter le TransporteurRepository si ce n'est pas fait
    @Autowired
    private TransporteurRepository transporteurRepository;

    @Override
    public Vehicule enregistrerVehiculeComplet(Vehicule vehicule, Long transporteurId) {
        // 1. On récupère le transporteur par son ID
        Transporteur transporteur = transporteurRepository.findById(transporteurId)
                .orElseThrow(() -> new RuntimeException("Transporteur non trouvé avec l'ID : " + transporteurId));

        // 2. On affecte le transporteur comme propriétaire du véhicule
        vehicule.setProprietaire(transporteur);

        // 3. On applique ta règle de validation sur la chargeMax
        if (vehicule.getChargeMax() != null && vehicule.getChargeMax() < 0) {
            throw new IllegalArgumentException("La charge maximale ne peut pas être négative");
        }

        // 4. On sauvegarde le tout
        return vehiculeRepository.save(vehicule);
    }
}