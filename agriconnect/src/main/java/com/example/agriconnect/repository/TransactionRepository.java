package com.example.agriconnect.repository;

import com.example.agriconnect.classes.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAcheteurId(Long acheteurId);
    // Dans TransactionRepository.java — remplace la requête existante
    @Query("""
    SELECT t FROM Transaction t
    JOIN t.vehicule v
    WHERE LOWER(t.villeArrivee) LIKE LOWER(CONCAT('%', :ville, '%'))
    AND (v.chargeMax - t.poidsActuel) >= :poids
    AND t.statut = 'PENDING'
""")
    List<Transaction> rechercherParVilleArrivee(
            @Param("ville") String ville,
            @Param("poids") Double poids
    );
}