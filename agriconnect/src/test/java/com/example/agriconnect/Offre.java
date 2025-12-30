package com.example.agriconnect;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Getter
@Setter
public class Offre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomProduit;
    private BigDecimal quantite;
    private String description;
    private String statut;

    private LocalDate datePublication;

    @ManyToOne
    @JoinColumn(name = "producteur_id")
    private Producteur producteur;
}
