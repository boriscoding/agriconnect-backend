package com.example.agriconnect.repository;

import com.example.agriconnect.classes.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {

    // Trouver un produit par son nom exact (ex: "Maïs")
    Optional<Produit> findByNom(String nom);

    // Lister tous les produits d'une catégorie (ex: "Céréales")
    List<Produit> findByCategorie(String categorie);

    // Rechercher des produits dont le nom contient une partie du texte (Recherche intuitive)
    List<Produit> findByNomContainingIgnoreCase(String nom);
}