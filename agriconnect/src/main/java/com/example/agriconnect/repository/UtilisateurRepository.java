package com.example.agriconnect.repository;

import com.example.agriconnect.classes.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    // Cette méthode magique va générer la requête SQL : SELECT * FROM utilisateurs WHERE email = ?
    Optional<Utilisateur> findByEmail(String email);
    // AJOUTE CETTE LIGNE :
    boolean existsByEmail(String email);

    Optional<Utilisateur> findByEmailIgnoreCase(String email);
        }