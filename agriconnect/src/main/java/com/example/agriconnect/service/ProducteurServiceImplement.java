package com.example.agriconnect.service;

import com.example.agriconnect.classes.Producteur;
import com.example.agriconnect.repository.ProducteurRepository;
import com.example.agriconnect.service.ProducteurService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProducteurServiceImplement implements ProducteurService{

    private final ProducteurRepository producteurRepository;

    public ProducteurServiceImplement(ProducteurRepository producteurRepository) {
        this.producteurRepository = producteurRepository;
    }

    @Override
    // Au lieu de findByEmail qui attend un seul résultat,
// On peut utiliser une liste si on a peur des doublons de test
    public Producteur findByEmailAndPassword(String email, String password) {
        List<Producteur> producteurs = producteurRepository.findAllByEmail(email);

        if (producteurs.isEmpty()) {
            throw new RuntimeException("Email non trouvé");
        }

        // On vérifie le mot de passe sur le premier trouvé
        Producteur p = producteurs.get(0);
        if (p.getPassword().equals(password)) {
            return p;
        } else {
            throw new RuntimeException("Mot de passe incorrect");
        }
    }
    @Override
    public Producteur create(Producteur producteur) {
        return producteurRepository.save(producteur);
    }

    @Override
    public List<Producteur> findAll() {
        return producteurRepository.findAll();
    }

    @Override
    public Producteur findById(Long id) {
        return producteurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producteur non trouvé"));
    }

    @Override
    public Producteur update(Long id, Producteur nouveauProducteur) {
        Producteur producteur = findById(id);
        producteur.setTypeProduit(nouveauProducteur.getTypeProduit());
        producteur.setSurfaceExploitation(nouveauProducteur.getSurfaceExploitation());
        return producteurRepository.save(producteur);
    }

    @Override
    public void delete(Long id) {
        producteurRepository.deleteById(id);
    }
}
