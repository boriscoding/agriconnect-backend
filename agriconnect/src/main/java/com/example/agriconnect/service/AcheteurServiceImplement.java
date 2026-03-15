package com.example.agriconnect.service;

import com.example.agriconnect.classes.Acheteur;
import com.example.agriconnect.repository.AcheteurRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcheteurServiceImplement implements  AcheteurService{
    private final AcheteurRepository acheteurRepository;

    public AcheteurServiceImplement(AcheteurRepository acheteurRepository) {
        this.acheteurRepository = acheteurRepository;
}
    @Override
    public Acheteur findByEmailAndPassword(String email, String password) {
        return acheteurRepository.findByEmail(email)
                .filter(a -> a.getPassword().equals(password))
                .orElseThrow(() -> new RuntimeException("Identifiants Acheteur incorrects"));
    }

    @Override
    public Acheteur creerAcheteur(Acheteur acheteur) {
        // On utilise le repository pour sauvegarder l'objet reçu
        // Puis on retourne l'objet sauvegardé (qui contient maintenant son ID)
        return acheteurRepository.save(acheteur);
    }

    @Override
    public Acheteur modifierAcheteur(Long id, Acheteur acheteur) {
        Acheteur existant = acheteurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Acheteur non trouvé"));

        existant.setAdresseLivraison(acheteur.getAdresseLivraison());
        existant.setNom(acheteur.getNom());
        existant.setEmail(acheteur.getEmail());

        return acheteurRepository.save(existant);
    }

    @Override
    public void supprimerAcheteur(Long id) {
        acheteurRepository.deleteById(id);
    }
    @Override
    public Acheteur getAcheteurById(Long id) {
        return acheteurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Acheteur non trouvé"));
    }

    @Override
    public List<Acheteur> getTousLesAcheteurs() {
        return acheteurRepository.findAll();
    }
}