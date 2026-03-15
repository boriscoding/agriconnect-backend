package com.example.agriconnect.service;

import com.example.agriconnect.classes.Transporteur;

import java.util.List;
public interface TransporteurService {
    Transporteur create(Transporteur transporteur);

    List<Transporteur> findAll();

    Transporteur findById(Long id);

    Transporteur update(Long id, Transporteur transporteur);

    void delete(Long id);
    Transporteur findByEmailAndPassword(String email, String password);
}
