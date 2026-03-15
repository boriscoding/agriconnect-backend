package com.example.agriconnect.service;

import com.example.agriconnect.classes.PositionGPS;
import com.example.agriconnect.repository.PositionGPSRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PositionGPSServiceImplement implements PositionGPSService{

    private final PositionGPSRepository repo;

    public PositionGPSServiceImplement(PositionGPSRepository repo) {
        this.repo = repo;
    }

    @Override
    public PositionGPS creer(PositionGPS positionGPS) {
        positionGPS.setHorodatage(LocalDateTime.now());
        return repo.save(positionGPS);
    }

    @Override
    public PositionGPS modifier(Long id, PositionGPS positionGPS) {
        PositionGPS existante = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Position GPS introuvable"));

        existante.setLatitude(positionGPS.getLatitude());
        existante.setLongitude(positionGPS.getLongitude());
        existante.setHorodatage(positionGPS.getHorodatage());

        return repo.save(existante);
    }

    @Override
    public PositionGPS getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Position GPS introuvable"));
    }

    @Override
    public List<PositionGPS> getToutes() {
        return repo.findAll();
    }

    @Override
    public List<PositionGPS> getParTransporteur(Long transporteurId) {
        return repo.findByTransporteurId(transporteurId);
    }

    @Override
    public void supprimer(Long id) {
        repo.deleteById(id);

    }
    }
