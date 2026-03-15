package com.example.agriconnect.repository;

import com.example.agriconnect.classes.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {
    // Exemple : Trouver les véhicules d'un transporteur précis
    List<Vehicule> findByProprietaireId(Long transporteurId);

    // Exemple : Trouver les véhicules par type (Camion, etc.)
    List<Vehicule> findByVoitureType(String type);
}