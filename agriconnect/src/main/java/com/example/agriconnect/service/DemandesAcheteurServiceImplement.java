package com.example.agriconnect.service;

import com.example.agriconnect.classes.Acheteur;
import com.example.agriconnect.classes.DemandesAcheteur;
import com.example.agriconnect.classes.Offre;
import com.example.agriconnect.repository.AcheteurRepository;
import com.example.agriconnect.repository.DemandesAcheteurRepository;
import com.example.agriconnect.repository.OffreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DemandesAcheteurServiceImplement implements DemandesAcheteurService {

    // On injecte tous les repositories nécessaires
    private final DemandesAcheteurRepository repository;
    private final AcheteurRepository acheteurRepository;
    private final OffreRepository offreRepository;

    @Override
    @Transactional
    public DemandesAcheteur creerDemandeDepuisParams(String libelle, double quantite, Long acheteurId, Long offreId) {
        // 1. Récupération des entités liées
        Acheteur acheteur = acheteurRepository.findById(acheteurId)
                .orElseThrow(() -> new RuntimeException("Acheteur non trouvé avec l'ID : " + acheteurId));

        Offre offre = offreRepository.findById(offreId)
                .orElseThrow(() -> new RuntimeException("Offre non trouvée avec l'ID : " + offreId));

        // 2. Construction de la demande
        DemandesAcheteur demande = new DemandesAcheteur();
        demande.setLibeller(libelle); // Assure-toi que l'attribut est 'libelle' dans l'entité
        demande.setQuantite_c(quantite);
        demande.setAcheteur(acheteur);
        demande.setOffre(offre);
        demande.setStatut("EN_ATTENTE");

        // 3. Sauvegarde
        return repository.save(demande);
    }

    @Override
    public DemandesAcheteur create(DemandesAcheteur demande) {
        if (demande.getStatut() == null) {
            demande.setStatut("EN_ATTENTE");
        }
        return repository.save(demande);
    }

    @Override
    public List<DemandesAcheteur> findAll() {
        return repository.findAll();
    }

    @Override
    public DemandesAcheteur findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée avec l'ID : " + id));
    }

    @Override
    @Transactional
    public DemandesAcheteur update(Long id, DemandesAcheteur demande) {
        DemandesAcheteur existing = findById(id);

        // Mise à jour des champs autorisés
        if (demande.getLibeller() != null) existing.setLibeller(demande.getLibeller());
        existing.setQuantite_c(demande.getQuantite_c());
        existing.setStatut(demande.getStatut());

        return repository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Suppression impossible : ID inexistant");
        }
        repository.deleteById(id);
    }

    @Override
    public List<DemandesAcheteur> findByAcheteurId(Long acheteurId) {
        return repository.findByAcheteurId(acheteurId);
    }
}