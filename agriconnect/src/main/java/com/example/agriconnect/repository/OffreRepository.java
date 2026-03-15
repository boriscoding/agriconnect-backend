package com.example.agriconnect.repository;

import com.example.agriconnect.classes.Offre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OffreRepository extends JpaRepository<Offre, Long>{
    // Offres d’un producteur
    List<Offre> findByProducteurId(Long producteurId);
    @Query("SELECT o FROM Offre o LEFT JOIN FETCH o.producteur")
    List<Offre> findAllWithProducteur();
    // Offres par statut
    List<Offre> findByStatut(String statut);
    // Cette méthode trie automatiquement par ID du plus grand au plus petit
    List<Offre> findAllByOrderByIdDesc();
}

