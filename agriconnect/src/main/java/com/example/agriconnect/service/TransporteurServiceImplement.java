package com.example.agriconnect.service;
import com.example.agriconnect.classes.Transporteur;
import com.example.agriconnect.repository.TransporteurRepository;
import com.example.agriconnect.service.TransporteurService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransporteurServiceImplement implements TransporteurService  {
    private final TransporteurRepository transporteurRepository;

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
