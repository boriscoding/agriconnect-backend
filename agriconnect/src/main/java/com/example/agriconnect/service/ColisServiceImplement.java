package com.example.agriconnect.service;

import com.example.agriconnect.classes.Colis;
import com.example.agriconnect.classes.Offre;
import com.example.agriconnect.classes.Transaction;
import com.example.agriconnect.classes.Transporteur;
import com.example.agriconnect.enumeration.StatutColis;
import com.example.agriconnect.repository.ColisRepository;
import com.example.agriconnect.repository.OffreRepository;
import com.example.agriconnect.repository.TransactionRepository;
import com.example.agriconnect.repository.TransporteurRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColisServiceImplement implements ColisService {

    private final ColisRepository colisRepository;
    private final OffreRepository offreRepository;
    private final TransactionRepository transactionRepository;
    private final TransporteurRepository transporteurRepository;

    @Autowired
    public ColisServiceImplement(ColisRepository colisRepository,
                                 OffreRepository offreRepository,
                                 TransactionRepository transactionRepository,
                                 TransporteurRepository transporteurRepository) {
        this.colisRepository = colisRepository;
        this.offreRepository = offreRepository;
        this.transactionRepository = transactionRepository;
        this.transporteurRepository = transporteurRepository;
    }

    @Override
    @Transactional
    public Colis traiterCommandeOffre(com.example.agriconnect.dto.CommandeOffreDTO dto) {
        // 1. Récupérer l'offre
        Offre offre = offreRepository.findById(dto.getOffreId())
                .orElseThrow(() -> new RuntimeException("Offre non trouvée"));

        Colis colis;

        // 2. Gérer le Colis (Existant ou Nouveau)
        if (dto.getColisId() != null) {
            colis = colisRepository.findById(dto.getColisId())
                    .orElseThrow(() -> new RuntimeException("Colis sélectionné introuvable"));
        } else {
            colis = new Colis();
            colis.setNom(dto.getNomNouveauColis());
            colis.setStatut(StatutColis.EN_ATTENTE);
        }

        // --- 3. LIAISON LOGISTIQUE (C'est ce qui manquait !) ---

        // Liaison avec la Transaction
        if (dto.getTransactionId() != null) {
            Transaction transaction = transactionRepository.findById(dto.getTransactionId())
                    .orElseThrow(() -> new RuntimeException("Transaction introuvable"));
            colis.setTransaction(transaction);
        }

        // Liaison avec le Transporteur
        if (dto.getTransporteurId() != null) {
            Transporteur transporteur = transporteurRepository.findById(dto.getTransporteurId())
                    .orElseThrow(() -> new RuntimeException("Transporteur introuvable"));
            colis.setTransporteur(transporteur);
        }

        // 4. Lier l'offre au colis
        offre.setColis(colis);
        offre.setStatut("EN_COLIS");

        if (!colis.getOffres().contains(offre)) {
            colis.getOffres().add(offre);
        }

        colis.calculerPoidsTotal();

        // 5. Sauvegarder
        return colisRepository.save(colis);
    }

    // --- Reste des méthodes ---
    @Override
    public Colis saveColis(Colis colis) { return colisRepository.save(colis); }

    @Override
    public List<Colis> getAllColis() { return colisRepository.findAll(); }

    @Override
    public Optional<Colis> getColisById(Long id) { return colisRepository.findById(id); }

    @Override
    public Colis updateColis(Long id, Colis colis) {
        return colisRepository.findById(id)
                .map(existingColis -> {
                    existingColis.setDimension(colis.getDimension());
                    existingColis.setPoids(colis.getPoids());
                    existingColis.setStatut(colis.getStatut());
                    existingColis.setTransporteur(colis.getTransporteur());
                    existingColis.setTransaction(colis.getTransaction());
                    return colisRepository.save(existingColis);
                })
                .orElseThrow(() -> new RuntimeException("Colis non trouvé avec id " + id));
    }

    @Override
    public void deleteColis(Long id) { colisRepository.deleteById(id); }
}