package com.example.agriconnect.service;

import com.example.agriconnect.classes.Transporteur;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface TransporteurService {
    Transporteur create(Transporteur transporteur);

    List<Transporteur> findAll();

    Transporteur findById(Long id);

    Transporteur update(Long id, Transporteur transporteur);

    void delete(Long id);
    Transporteur findByEmailAndPassword(String email, String password);
    public Transporteur modifierProfilAvecImage(Long id, Map<String, Object> updates, MultipartFile file);
}
