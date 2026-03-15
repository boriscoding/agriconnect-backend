package com.example.agriconnect.repository;
import com.example.agriconnect.classes.PositionGPS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
public interface PositionGPSRepository extends JpaRepository<PositionGPS, Long> {
    // Toutes les positions d’un transporteur
    List<PositionGPS> findByTransporteurId(Long transporteurId);
}
