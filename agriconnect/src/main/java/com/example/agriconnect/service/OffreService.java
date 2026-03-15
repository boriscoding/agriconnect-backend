package com.example.agriconnect.service;

import com.example.agriconnect.classes.Offre;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// On enlève @Service ici
public interface OffreService {

    Offre creer(Offre offre);

    Offre modifier(Long id, Offre offre);

    Offre getById(Long id);

    List<Offre> getToutes();

    List<Offre> getParProducteur(Long producteurId);

    void supprimer(Long id);

    // Méthode pour la saisie libre sans table Produit
    Offre enregistrerDirectement(Offre offre, Long producteurId, MultipartFile file);
    public List<Offre> recupererToutesLesOffres() ;
    public void supprimerOffre(Long id);
    public Offre modifierOffre(Long id, Offre nouvellesInfos);
}