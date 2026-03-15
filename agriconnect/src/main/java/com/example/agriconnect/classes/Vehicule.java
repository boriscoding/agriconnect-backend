package com.example.agriconnect.classes;

import com.example.agriconnect.classes.Transporteur;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicules")
@Data // Remplace Getter/Setter/ToString/Equals
@NoArgsConstructor
@AllArgsConstructor
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marque;
    private String model;
    private String immatriculation;

    private String voitureType; // Ex: Camion, Pick-up, Moto
    private String description;

    @Column(name = "charge_max")
    private Double chargeMax; // Capacité en Tonnes

    private LocalDateTime dateEnregistrement;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_transporteur", nullable = false)
    private Transporteur proprietaire;

    // S'exécute automatiquement avant l'insertion en base
    @PrePersist
    protected void onCreate() {
        this.dateEnregistrement = LocalDateTime.now();
    }
    private String mediaUrl;
    private String photo ;
}