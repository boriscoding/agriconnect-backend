package com.example.agriconnect.service;
import com.example.agriconnect.classes.Alerte;
import com.example.agriconnect.repository.AlerteRepository;
import com.example.agriconnect.service.AlerteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlerteServiceImplement implements AlerteService {
    private final AlerteRepository alerteRepository;

    public AlerteServiceImplement(AlerteRepository alerteRepository) {
        this.alerteRepository = alerteRepository;
    }

    @Override
    public Alerte create(Alerte alerte) {
        return alerteRepository.save(alerte);
    }

    @Override
    public List<Alerte> findAll() {
        return alerteRepository.findAll();
    }

    @Override
    public Alerte findById(Long id) {
        return alerteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alerte non trouvée"));
    }

    @Override
    public Alerte update(Long id, Alerte nouvelleAlerte) {
        Alerte alerte = findById(id);

        alerte.setType(nouvelleAlerte.getType());
        alerte.setMessage(nouvelleAlerte.getMessage());
        alerte.setDate(nouvelleAlerte.getDate());
        alerte.setColis(nouvelleAlerte.getColis());

        return alerteRepository.save(alerte);
    }

    @Override
    public void delete(Long id) {
        alerteRepository.deleteById(id);
    }
}
