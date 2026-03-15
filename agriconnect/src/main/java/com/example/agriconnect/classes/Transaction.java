package com.example.agriconnect.classes;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reference;      // Identifiant unique (ex: TX-2026-XYZ)
    private Double montantTotal;   // Somme figée au moment du paiement
    private String statut;         // PENDING, SUCCESS, FAILED
    private LocalDate date;

    // Itinéraire commun pour le groupage
    private String villeDepart;
    private String villeArrivee;

    @ManyToOne
    @JoinColumn(name = "acheteur_id")
    private Acheteur acheteur;

    // Une transaction regroupe plusieurs colis
    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL)

    private List<Colis> colisRegroupes;

    @ManyToOne
    @JoinColumn(name = "transporteur_id")
    private Transporteur transporteur; // <--- AJOUTE CECI
}