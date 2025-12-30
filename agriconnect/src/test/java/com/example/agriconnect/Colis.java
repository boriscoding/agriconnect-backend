package com.example.agriconnect;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Colis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private BigDecimal quantite;
    private String pointDepart;
    private String pointArrivee;

    @OneToOne
    private Transaction transaction;
}
