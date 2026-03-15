package com.example.agriconnect.service;

import com.example.agriconnect.classes.DemandesAcheteur;
import com.example.agriconnect.repository.DemandesAcheteurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DemandesAcheteurServiceImplement implements DemandesAcheteurService {

    private final DemandesAcheteurRepository repository;

    @Override
    public DemandesAcheteur create(DemandesAcheteur demande) {
        return repository.save(demande);
    }

    @Override
    public List<DemandesAcheteur> findAll() {
        return repository.findAll();
    }

    @Override
    public DemandesAcheteur findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));
    }

    @Override
    public DemandesAcheteur update(Long id, DemandesAcheteur demande) {
        DemandesAcheteur existing = findById(id);
        existing.setQuantite_c(demande.getQuantite_c());
        existing.setStatut(demande.getStatut());
        return repository.save(existing);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<DemandesAcheteur> findByAcheteurId(Long acheteurId) {
        return repository.findByAcheteurId(acheteurId);
    }
}