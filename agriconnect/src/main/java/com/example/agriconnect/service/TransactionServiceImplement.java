package com.example.agriconnect.service;

import com.example.agriconnect.classes.*;
import com.example.agriconnect.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class TransactionServiceImplement implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final ColisRepository colisRepository;
    private final AcheteurRepository acheteurRepository;

    @Override
    public Transaction create(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction findById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction non trouvée"));
    }

    @Override
    public Transaction update(Long id, Transaction transaction) {
        Transaction existing = findById(id);
        existing.setStatut(transaction.getStatut());
        existing.setMontantTotal(transaction.getMontantTotal());
        // Ajoute d'autres champs si nécessaire
        return transactionRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        transactionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Transaction creerGroupage(Long acheteurId, List<Long> colisIds, String depart, String arrivee) {
        // ... (Ton code de groupage est déjà parfait ici) ...
        Acheteur acheteur = acheteurRepository.findById(acheteurId)
                .orElseThrow(() -> new RuntimeException("Acheteur non trouvé"));

        List<Colis> colisSelectionnes = colisRepository.findAllById(colisIds);

        Transaction transaction = new Transaction();
        transaction.setReference("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        transaction.setAcheteur(acheteur);
        transaction.setVilleDepart(depart);
        transaction.setVilleArrivee(arrivee);
        transaction.setDate(LocalDate.now());
        transaction.setStatut("SUCCESS");

        double total = colisSelectionnes.stream()
                .mapToDouble(c -> (c.getCommande() != null) ?
                        c.getCommande().getQuantite_c() * c.getCommande().getOffre().getPrixUnitaire() : 0.0)
                .sum();

        transaction.setMontantTotal(total);
        Transaction savedTx = transactionRepository.save(transaction);

        for (Colis colis : colisSelectionnes) {
            colis.setTransaction(savedTx);
            colisRepository.save(colis);
        }
        return savedTx;
    }

    @Override
    public List<Transaction> getHistoriqueAcheteur(Long acheteurId) {
        return transactionRepository.findByAcheteurId(acheteurId);
    }

    @Override
    public Transaction getByReference(String reference) {
        // Assure-toi que cette méthode existe dans TransactionRepository
        return null;
    }
}