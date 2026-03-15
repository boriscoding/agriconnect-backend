package com.example.agriconnect.service;
import com.example.agriconnect.classes.Alerte;

import java.util.List;
public interface AlerteService {
    Alerte create(Alerte alerte);

    List<Alerte> findAll();

    Alerte findById(Long id);

    Alerte update(Long id, Alerte alerte);

    void delete(Long id);
}
