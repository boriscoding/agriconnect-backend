package com.example.agriconnect.repository;

import com.example.agriconnect.classes.PartageDiscussion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PartageDiscussionRepository extends JpaRepository<PartageDiscussion, Long> {

    @Query("SELECT p FROM PartageDiscussion p WHERE p.initiateurId = :initiateurId AND p.beneficiaireId = :beneficiaireId AND p.tiersId = :tiersId AND p.actif = true")
    PartageDiscussion findActif(
            @Param("initiateurId") Long initiateurId,
            @Param("beneficiaireId") Long beneficiaireId,
            @Param("tiersId") Long tiersId);
}