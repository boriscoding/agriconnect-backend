package com.example.agriconnect.repository;

import com.example.agriconnect.classes.DemandesAcheteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DemandesAcheteurRepository extends JpaRepository<DemandesAcheteur, Long> {

    /**
     * Permet de récupérer toutes les demandes (panier) d'un acheteur spécifique.
     * Spring Boot génère automatiquement la requête SQL grâce au nom de la méthode.
     */
    List<DemandesAcheteur> findByAcheteurId(Long acheteurId);

    /**
     * Optionnel : Trouver les demandes par statut (ex: "PENDING", "VALIDATED")
     */
    List<DemandesAcheteur> findByStatut(String statut);

}