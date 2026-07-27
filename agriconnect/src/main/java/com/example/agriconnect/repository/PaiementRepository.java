package com.example.agriconnect.repository;

import com.example.agriconnect.classes.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    // Utile pour vérifier le statut quand l'API de paiement nous répond
    Optional<Paiement> findByReferenceTransaction(String reference);
}