package com.example.agriconnect;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String statut;
    private LocalDateTime dateTransaction;

    @ManyToOne
    private Acheteur acheteur;

    @ManyToOne
    private Producteur producteur;

    @ManyToOne
    private Transporteur transporteur;
}
