package com.example.agriconnect.service;

import com.example.agriconnect.classes.DemandesAcheteur;
import java.util.List;

public interface DemandesAcheteurService {
    DemandesAcheteur create(DemandesAcheteur demande);
    List<DemandesAcheteur> findAll();
    DemandesAcheteur findById(Long id);
    DemandesAcheteur update(Long id, DemandesAcheteur demande);
    void delete(Long id);
    List<DemandesAcheteur> findByAcheteurId(Long acheteurId);
}