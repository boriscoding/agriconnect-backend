package com.example.agriconnect.service;
import com.example.agriconnect.classes.PositionGPS;

import java.util.List;

public interface PositionGPSService {
    PositionGPS creer(PositionGPS positionGPS);

    PositionGPS modifier(Long id, PositionGPS positionGPS);

    PositionGPS getById(Long id);

    List<PositionGPS> getToutes();

    List<PositionGPS> getParTransporteur(Long transporteurId);

    void supprimer(Long id);
}
