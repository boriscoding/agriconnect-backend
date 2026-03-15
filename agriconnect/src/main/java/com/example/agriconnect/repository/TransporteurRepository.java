package com.example.agriconnect.repository;


import com.example.agriconnect.classes.Transporteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransporteurRepository extends JpaRepository<Transporteur, Long>{
    // Cette méthode permet de chercher un transporteur par son email
    Optional<Transporteur> findByEmail(String email);
}
