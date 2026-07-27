package com.example.agriconnect.classes;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Offre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Double quantiteRestante;

    // Optionnel : Initialisation automatique lors de la création
    @PrePersist
    public void prePersist() {
        if (this.quantiteRestante == null) {
            this.quantiteRestante = this.quantiteProduit;
        }
    }

    private String titre;
    private String Nproduit;      // Le nom du produit saisi manuellement (ex: "Tomates de Foumbot")
    private String description;
    private Double prixUnitaire;
    private Double quantiteProduit;
    private String lieuProduction;
    private String statut;
    private LocalDate datePublication;
private String  type_pro ;
    // Images
    private String photo;
    private String mediaUrl;

    // RELATION : On garde uniquement le producteur


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producteur_id")
    @JsonIgnoreProperties("offres") //
    private Producteur producteur;
    // ... tes autres attributs ...

    @JsonIgnore  // ← AJOUTE CECI
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colis_id")
    @JsonIgnoreProperties("offres")
    private Colis colis;
}