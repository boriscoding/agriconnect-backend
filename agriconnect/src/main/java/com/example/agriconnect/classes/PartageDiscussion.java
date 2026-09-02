package com.example.agriconnect.classes;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class PartageDiscussion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long initiateurId;   // Utilisateur A (Celui qui partage sa discussion)
    private Long beneficiaireId; // Utilisateur C (Celui qui reçoit l'accès)
    private Long tiersId;        // Utilisateur B (Le contact avec qui A discute)

    private LocalDateTime dateDebutAcces; // Date à partir de laquelle C peut lire les messages
    private boolean actif = true;
}