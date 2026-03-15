package com.example.agriconnect.service;

import com.example.agriconnect.classes.Colis;
import com.example.agriconnect.repository.ColisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColisServiceImplement  implements ColisService {

    private final ColisRepository colisRepository;

    @Autowired
    public ColisServiceImplement(ColisRepository colisRepository) {
        this.colisRepository = colisRepository;
    }

    @Override
    public Colis saveColis(Colis colis) {
        return colisRepository.save(colis);
    }

    @Override
    public List<Colis> getAllColis() {
        return colisRepository.findAll();
    }

    @Override
    public Optional<Colis> getColisById(Long id) {
        return colisRepository.findById(id);
    }

    @Override
    public Colis updateColis(Long id, Colis colis) {
        return colisRepository.findById(id)
                .map(existingColis -> {
                    existingColis.setDimension(colis.getDimension());
                    existingColis.setPoids(colis.getPoids());
                    existingColis.setStatut(colis.getStatut());
                    existingColis.setTransporteur(colis.getTransporteur());
                    return colisRepository.save(existingColis);
                })
                .orElseThrow(() -> new RuntimeException("Colis non trouvé avec id " + id));
    }

    @Override
    public void deleteColis(Long id) {
        colisRepository.deleteById(id);
    }
}
