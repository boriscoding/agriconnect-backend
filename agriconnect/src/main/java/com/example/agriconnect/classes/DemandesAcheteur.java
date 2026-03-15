package com.example.agriconnect.classes;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@Entity
@Table(name = "dacheteur")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class DemandesAcheteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double quantite_c; // La quantité que l'acheteur veut (ex: 10 sacs)
    private  String statut ;
    @ManyToOne
    @JoinColumn(name = "offre_id", nullable = false)
    private Offre offre; // L'offre spécifique choisie

    @ManyToOne
    @JoinColumn(name = "colis_id")
    private Colis colis; // Le colis qui contient cette demande
    @ManyToOne
    private Acheteur acheteur; // Le nom 'acheteur' ici doit correspondre au 'Acheteur' dans findByAcheteurId
}