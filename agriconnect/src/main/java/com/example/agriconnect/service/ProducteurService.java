package com.example.agriconnect.service;

import com.example.agriconnect.classes.Producteur;

import java.util.List;
public interface ProducteurService {

    Producteur create(Producteur producteur);

    List<Producteur> findAll();

    Producteur findById(Long id);

    Producteur update(Long id, Producteur producteur);

    void delete(Long id);
    Producteur findByEmailAndPassword(String email, String password);
}
