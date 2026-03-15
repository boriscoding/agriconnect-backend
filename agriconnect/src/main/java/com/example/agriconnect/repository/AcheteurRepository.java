package com.example.agriconnect.repository;

import com.example.agriconnect.classes.Acheteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcheteurRepository  extends JpaRepository<Acheteur, Long>{
    Optional<Acheteur> findByEmail(String email);
}
