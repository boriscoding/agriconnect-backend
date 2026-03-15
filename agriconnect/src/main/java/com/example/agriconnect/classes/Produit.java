package com.example.agriconnect.classes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "produit")
public class Produit {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String nom;       // ex: "Maïs"
        private String categorie; // ex: "Céréales"

}
