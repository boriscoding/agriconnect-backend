package com.example.agriconnect.service;

import com.example.agriconnect.classes.Producteur;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ProducteurService {

    Producteur create(Producteur producteur);

    List<Producteur> findAll();

    Producteur findById(Long id);

    Producteur update(Long id, Producteur producteur);

    void delete(Long id);
    Producteur findByEmailAndPassword(String email, String password);
    public Producteur modifierProfilAvecImage(Long id, Map<String, Object> updates, MultipartFile file);
}
