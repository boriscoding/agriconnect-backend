package com.example.agriconnect.service;

import com.example.agriconnect.classes.Paiement;
import com.example.agriconnect.repository.PaiementRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class PaiementServiceImpl implements PaiementService {

    private final PaiementRepository paiementRepository;

    public PaiementServiceImpl(PaiementRepository paiementRepository) {
        this.paiementRepository = paiementRepository;
    }

    @Override
    public String initialiserPaiement(Double montant, String mode, Long acheteurId, Long demandeId) {
        // 1. Génération d'une référence unique
        String reference = "AGRI-" + UUID.randomUUID().toString().substring(0, 8);

        // 2. Création de l'objet Paiement en base de données
        Paiement paiement = new Paiement();
        paiement.setMontant(montant);
        paiement.setModePaiement(mode);
        paiement.setReferenceTransaction(reference);
        paiement.setStatut("EN_ATTENTE");

        // Note: Ici tu devrais récupérer l'Acheteur et la Demande via leurs Repositories
        // paiement.setAcheteur(userRepo.findById(acheteurId).orElse(null));

        paiementRepository.save(paiement);

        // 3. Simulation du lien de paiement (Exemple: Monetbil ou CinetPay)
        // En réel, tu ferais un appel HTTP ici pour obtenir ce lien
        return "https://www.monetbil.com/pay/v2.1/" + reference;
    }

    @Override
    public boolean verifierStatut(String reference) {
        return paiementRepository.findByReferenceTransaction(reference)
                .map(p -> "SUCCES".equals(p.getStatut()))
                .orElse(false);
    }
}