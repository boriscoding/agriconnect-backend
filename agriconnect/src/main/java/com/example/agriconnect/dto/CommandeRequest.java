package com.example.agriconnect.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class CommandeRequest {
    private Long offreId;
    private Double quantite;
    private Long destinataireId; // AJOUT : L'ID de l'acheteur/destinataire
    private Long clientId;
    private Long transactionTransportId;
    private String modeColis;
    private Long colisId;
    private String nomNouveauColis;
    private String destination;
}