package com.example.agriconnect.service;
import com.example.agriconnect.classes.Transporteur;
import com.example.agriconnect.repository.TransporteurRepository;
import com.example.agriconnect.service.TransporteurService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TransporteurServiceImplement implements TransporteurService  {
    private final TransporteurRepository transporteurRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate; // 👈 AJOUTE CETTE LIGNE

    @Override
    @Transactional
    public Transporteur modifierProfilAvecImage(Long id, Map<String, Object> updates, MultipartFile file) {
        return transporteurRepository.findById(id).map(transporteur -> {

            // 1. Mise à jour des champs communs
            if (updates.get("nom") != null) transporteur.setNom((String) updates.get("nom"));
            if (updates.get("email") != null) transporteur.setEmail((String) updates.get("email"));
            if (updates.get("localisation") != null) transporteur.setLocalisation((String) updates.get("localisation"));
            if (updates.get("sexe") != null) transporteur.setSexe((String) updates.get("sexe"));

            if (updates.get("number") != null) {
                transporteur.setNumber(Integer.valueOf(updates.get("number").toString()));
            }

            // 2. Mise à jour des champs spécifiques (Transporteur)
            if (updates.get("typeVehicule") != null) {
                transporteur.setTypeVehicule((String) updates.get("typeVehicule"));
            }
            if (updates.get("capaciteMax") != null) {
                transporteur.setCapaciteMax(Double.valueOf(updates.get("capaciteMax").toString()));
            }

            // 3. Gestion physique de l'image
            if (file != null && !file.isEmpty()) {
                try {
                    String originalName = file.getOriginalFilename();
                    String extension = (originalName != null && originalName.contains(".")) ?
                            originalName.substring(originalName.lastIndexOf(".")) : "";

                    String fileName = UUID.randomUUID().toString() + extension;
                    Path path = Paths.get("C:/Users/User/Documents/agriconnect (1)/uploads/" + fileName);

                    if (!Files.exists(path.getParent())) {
                        Files.createDirectories(path.getParent());
                    }

                    Files.write(path, file.getBytes());
                    transporteur.setPhotosuser("/uploads/" + fileName);

                } catch (IOException e) {
                    throw new RuntimeException("Erreur de stockage physique : " + e.getMessage());
                }
            }

            // 4. Sauvegarde
            Transporteur saved = transporteurRepository.save(transporteur);

            // 5. Notification WebSocket (C'est ici que ça plantait car messagingTemplate était null)
            if (messagingTemplate != null) {
                messagingTemplate.convertAndSend("/topic/user-update/" + id, saved);
            }

            return saved;
        }).orElseThrow(() -> new RuntimeException("Transporteur introuvable avec l'ID : " + id));
    }
    public TransporteurServiceImplement(TransporteurRepository transporteurRepository) {
        this.transporteurRepository = transporteurRepository;
    }
    @Override
    public Transporteur findByEmailAndPassword(String email, String password) {
        return transporteurRepository.findByEmail(email)
                .filter(t -> t.getPassword().equals(password))
                .orElseThrow(() -> new RuntimeException("Identifiants Transporteur incorrects"));
    }

    @Override
    public Transporteur create(Transporteur transporteur) {
        return transporteurRepository.save(transporteur);
    }

    @Override
    public List<Transporteur> findAll() {
        return transporteurRepository.findAll();
    }

    @Override
    public Transporteur findById(Long id) {
        return transporteurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transporteur non trouvé"));
    }

    @Override
    public Transporteur update(Long id, Transporteur nouveauTransporteur) {
        Transporteur transporteur = findById(id);

        transporteur.setTypeVehicule(nouveauTransporteur.getTypeVehicule());
        transporteur.setCapaciteMax(nouveauTransporteur.getCapaciteMax());
        transporteur.setColis(nouveauTransporteur.getColis());

        return transporteurRepository.save(transporteur);
    }

    @Override
    public void delete(Long id) {
        transporteurRepository.deleteById(id);
    }


}
