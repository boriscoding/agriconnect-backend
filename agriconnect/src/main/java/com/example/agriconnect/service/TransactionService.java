package com.example.agriconnect.service;

import com.example.agriconnect.classes.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction create(Transaction transaction);

    List<Transaction> findAll();

    Transaction findById(Long id);

    Transaction update(Long id, Transaction transaction);

    void delete(Long id);

    // Créer un groupage de colis et figer le montant
    Transaction creerGroupage(Long acheteurId, List<Long> colisIds, String depart, String arrivee);

    // Récupérer l'historique d'un acheteur
    List<Transaction> getHistoriqueAcheteur(Long acheteurId);

    // Trouver une transaction par sa référence unique
    Transaction getByReference(String reference);
//    public List<Transaction> rechercherTransactionsPourOffre(String ville, Double poids);
    List<Transaction> rechercherTrajets(String ville, Double poids);
}
