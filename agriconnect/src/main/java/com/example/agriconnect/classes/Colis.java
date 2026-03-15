package com.example.agriconnect.classes;

import com.example.agriconnect.enumeration.StatutColis;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Colis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dimension;
    private Double poids;

    @Enumerated(EnumType.STRING)
    private StatutColis statut;

    @ManyToOne
    @JoinColumn(name = "transporteur_id")
    @JsonIgnore // Empêche de remonter vers le transporteur lors de la lecture
    private Transporteur transporteur;

    @ManyToOne
    @JoinColumn(name = "demandesacheteur_id")
    @JsonIgnore // Empêche de remonter vers la commande
    private DemandesAcheteur commande;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    @JsonIgnore // Empêche de remonter vers la transaction
    private Transaction transaction;
}
