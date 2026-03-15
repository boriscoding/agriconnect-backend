package com.example.agriconnect.repository;

import com.example.agriconnect.classes.Abonnement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AbonnementRepository extends JpaRepository<Abonnement, Long> {
    Optional<Abonnement> findByUtilisateurId(Long utilisateurId);
}
