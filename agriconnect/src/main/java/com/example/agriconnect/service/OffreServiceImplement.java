package com.example.agriconnect.service;

import com.example.agriconnect.classes.Offre;
import com.example.agriconnect.classes.Producteur;
import com.example.agriconnect.repository.OffreRepository;
import com.example.agriconnect.repository.ProducteurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import java.util.UUID;
import java.io.IOException;

@Service
@RequiredArgsConstructor // Remplace les @Autowired manuels pour plus de propreté
public class OffreServiceImplement implements OffreService {

    private final OffreRepository repo;
    private final ProducteurRepository producteurRepository;

    @Override
    public Offre creer(Offre offre) {
        offre.setDatePublication(LocalDate.now());
        if (offre.getStatut() == null) {
            offre.setStatut("ACTIVE");
        }
        return repo.save(offre);
    }

    @Override
    public Offre modifier(Long id, Offre offre) {
        Offre existante = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Offre introuvable"));

        // On utilise les nouveaux champs String (Nproduit et type_pro)
        existante.setTitre(offre.getTitre());
        existante.setNproduit(offre.getNproduit());
        existante.setType_pro(offre.getType_pro());
        existante.setQuantiteProduit(offre.getQuantiteProduit());
        existante.setPrixUnitaire(offre.getPrixUnitaire());
        existante.setDescription(offre.getDescription());
        existante.setStatut(offre.getStatut());
        existante.setLieuProduction(offre.getLieuProduction());

        return repo.save(existante);
    }

    @Override
    public Offre getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Offre introuvable"));
    }

    @Override
    public List<Offre> getToutes() {
        return repo.findAll();
    }

    @Override
    public List<Offre> getParProducteur(Long producteurId) {
        return repo.findByProducteurId(producteurId);
    }

    @Override
    public void supprimer(Long id) {
        repo.deleteById(id);
    }

    /**
     * Cette méthode est celle utilisée par ton OffreController.
     * Elle ne fait plus appel à ProduitRepository car tu saisis le nom manuellement.
     */
    // 1. On définit le chemin absolu en haut de la méthode ou en variable de classe
    private final String UPLOAD_PATH = "C:/Users/User/Documents/agriconnect (1)/uploads/";

    @Override
    public Offre enregistrerDirectement(Offre offre, Long producteurId, MultipartFile file) {
        // Récupérer le producteur
        offre.setId(null);
        Producteur p = producteurRepository.findById(producteurId)
                .orElseThrow(() -> new RuntimeException("Producteur non trouvé avec l'id : " + producteurId));

        offre.setProducteur(p);

        // Gérer l'enregistrement du fichier
        if (file != null && !file.isEmpty()) {
            try {
                // Créer le dossier s'il n'existe pas
                Path root = Paths.get(UPLOAD_PATH);
                if (!Files.exists(root)) {
                    Files.createDirectories(root);
                }

                // Générer un nom unique
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

                // Sauvegarde PHYSIQUE à l'endroit précis
                Files.copy(file.getInputStream(), root.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

                // Stocker le nom dans la base de données
                offre.setPhoto(fileName);

            } catch (IOException e) {
                throw new RuntimeException("Erreur lors de la sauvegarde du fichier image dans : " + UPLOAD_PATH, e);
            }
        }

        // Sécurité date et statut
        if (offre.getDatePublication() == null) {
            offre.setDatePublication(LocalDate.now());
        }
        if (offre.getStatut() == null) {
            offre.setStatut("ACTIVE");
        }

        return repo.save(offre);
    }
    @Override

    public List<Offre> recupererToutesLesOffres() {
        return repo.findAllByOrderByIdDesc();
    }

// Dans ton OffreService.java

    public void supprimerOffre(Long id) {
        repo.deleteById(id);
    }

    public Offre modifierOffre(Long id, Offre nouvellesInfos) {
        return repo.findById(id).map(offre -> {
            // On met à jour uniquement les champs modifiables
            offre.setNproduit(nouvellesInfos.getNproduit());
            offre.setPrixUnitaire(nouvellesInfos.getPrixUnitaire());
            offre.setDescription(nouvellesInfos.getDescription());
            offre.setStatut(nouvellesInfos.getStatut());
            offre.setQuantiteProduit(nouvellesInfos.getQuantiteProduit());
            offre.setLieuProduction(nouvellesInfos.getLieuProduction());

            return repo.save(offre);
        }).orElseThrow(() -> new RuntimeException("Offre non trouvée avec l'id " + id));
    }
}