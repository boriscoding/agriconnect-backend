package com.example.agriconnect.classes;
import com.example.agriconnect.classes.DemandesAcheteur;
import com.example.agriconnect.classes.Utilisateur;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "paiements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double montant;

    @Column(length = 1000 ,unique = true) // Augmente la capacité à 1000 caractères
    private String referenceTransaction; // La référence renvoyée par CinetPay/Stripe

    private String modePaiement; // ex: Orange Money, MTN, CARTE

    private String statut; // ex: SUCCES, ECHEC, EN_ATTENTE

    private LocalDateTime datePaiement;

    @ManyToOne
    @JoinColumn(name = "acheteur_id")
    private Utilisateur acheteur;

    @OneToOne
    @JoinColumn(name = "demande_id")
    private DemandesAcheteur demande;

    @PrePersist
    protected void onCreate() {
        datePaiement = LocalDateTime.now();
        if (statut == null) statut = "EN_ATTENTE";
    }
}