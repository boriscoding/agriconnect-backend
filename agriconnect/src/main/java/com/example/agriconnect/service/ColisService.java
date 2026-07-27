package com.example.agriconnect.service;

import com.example.agriconnect.classes.Colis;
import java.util.List;
import java.util.Optional;

public interface ColisService {
    Colis saveColis(Colis colis);

    List<Colis> getAllColis();

    Optional<Colis> getColisById(Long id);

    Colis updateColis(Long id, Colis colis);

    void deleteColis(Long id);
    public Colis traiterCommandeOffre(com.example.agriconnect.dto.CommandeOffreDTO dto);
}
