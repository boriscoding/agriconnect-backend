package com.example.agriconnect.service;

import com.example.agriconnect.classes.Abonnement;
import com.example.agriconnect.repository.AbonnementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbonnementServiceImplement  implements AbonnementService {
    private final AbonnementRepository abonnementRepository;

    public AbonnementServiceImplement(AbonnementRepository abonnementRepository) {
        this.abonnementRepository = abonnementRepository;
    }

    @Override
    public Abonnement creerAbonnement(Abonnement abonnement) {
        return abonnementRepository.save(abonnement);
    }

    @Override
    public Abonnement modifierAbonnement(Long id, Abonnement abonnement) {
        Abonnement existant = abonnementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Abonnement non trouvé"));

        existant.setStatut(abonnement.getStatut());
        existant.setTypeAbonnement(abonnement.getTypeAbonnement());
        existant.setDateDebut(abonnement.getDateDebut());
        existant.setDateFin(abonnement.getDateFin());
        existant.setUtilisateur(abonnement.getUtilisateur());

        return abonnementRepository.save(existant);
    }

    @Override
    public void supprimerAbonnement(Long id) {
        abonnementRepository.deleteById(id);
    }

    @Override
    public Abonnement getAbonnementById(Long id) {
        return abonnementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Abonnement non trouvé"));
    }

    @Override
    public List<Abonnement> getTousLesAbonnements() {
        return abonnementRepository.findAll();
    }

    @Override
    public Abonnement getAbonnementParUtilisateur(Long utilisateurId) {
        return abonnementRepository.findByUtilisateurId(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Aucun abonnement pour cet utilisateur"));
    }
}
