package com.example.agriconnect.classes;

import java.time.LocalDate;

public class CommandeOffreDTO {
    // --- PARTIE 1 : L'OFFRE (La Source) ---
    private Long offreId;
    private Long producteurId;

    // --- PARTIE 2 : LE COLIS (Le Regroupement) ---
    private Long colisId;           // Si existant : ID du colis choisi
    private String nomNouveauColis; // Si nouveau : Nom donné par le producteur
    private String villeDepart;     // Pour valider la trajectoire
    private String villeArrivee;    // Pour valider la trajectoire

    // --- PARTIE 3 : LA TRANSACTION (Le Voyage) ---
    private Long transactionId;      // Si existant : ID de la transaction sur le trajet
    private LocalDate dateDepart;    // Si nouvelle : Date prévue du voyage

    // --- PARTIE 4 : LE VEHICULE (Le Transport) ---
    private Long vehiculeId;        // ID du véhicule choisi (vérifié par capacité et disponibilité)
    private Long transporteurId;    // ID du transporteur (pour le suivi GPS futur)

    // --- PARTIE 5 : INFOS LOGISTIQUES ---
    private Double poidsTotalOffre; // Pour vérifier si ça rentre dans le véhicule

    public Long getOffreId() {
        return offreId;
    }

    public void setOffreId(Long offreId) {
        this.offreId = offreId;
    }

    public Long getProducteurId() {
        return producteurId;
    }

    public void setProducteurId(Long producteurId) {
        this.producteurId = producteurId;
    }

    public Long getColisId() {
        return colisId;
    }

    public void setColisId(Long colisId) {
        this.colisId = colisId;
    }

    public String getNomNouveauColis() {
        return nomNouveauColis;
    }

    public void setNomNouveauColis(String nomNouveauColis) {
        this.nomNouveauColis = nomNouveauColis;
    }

    public String getVilleDepart() {
        return villeDepart;
    }

    public void setVilleDepart(String villeDepart) {
        this.villeDepart = villeDepart;
    }

    public String getVilleArrivee() {
        return villeArrivee;
    }

    public void setVilleArrivee(String villeArrivee) {
        this.villeArrivee = villeArrivee;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDate getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(LocalDate dateDepart) {
        this.dateDepart = dateDepart;
    }

    public Long getVehiculeId() {
        return vehiculeId;
    }

    public void setVehiculeId(Long vehiculeId) {
        this.vehiculeId = vehiculeId;
    }

    public Long getTransporteurId() {
        return transporteurId;
    }

    public void setTransporteurId(Long transporteurId) {
        this.transporteurId = transporteurId;
    }

    public Double getPoidsTotalOffre() {
        return poidsTotalOffre;
    }

    public void setPoidsTotalOffre(Double poidsTotalOffre) {
        this.poidsTotalOffre = poidsTotalOffre;
    }
}
