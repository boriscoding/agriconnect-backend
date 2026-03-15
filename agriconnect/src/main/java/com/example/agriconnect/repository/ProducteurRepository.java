package com.example.agriconnect.repository;
import com.example.agriconnect.classes.Producteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProducteurRepository extends JpaRepository<Producteur, Long>  {
    // Cette ligne permet à Spring de savoir comment chercher par Email
    Optional<Producteur> findByEmail(String email);
    List<Producteur> findAllByEmail(String email);
}
