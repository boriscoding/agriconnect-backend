package com.example.agriconnect.repository;
import com.example.agriconnect.classes.Message;
import com.example.agriconnect.classes.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    // L'underscore permet à Spring de comprendre : "va dans l'objet destinataire et prend son Id"
    List<Message> findByDestinataire_Id(Long destinataireId);
    List<Message> findByExpediteur_Id(Long expediteurId);
    /**
     * CETTE MÉTHODE RÉCUPÈRE TOUTE LA CONVERSATION
     * Elle cherche les messages où :
     * (Expéditeur = A ET Destinataire = B)
     * OU
     * (Expéditeur = B ET Destinataire = A)
     * Le tout trié par date d'envoi pour l'affichage chronologique.
     */

    @Query("SELECT DISTINCT u FROM Utilisateur u WHERE u.id IN (" +
            "SELECT CASE WHEN m.expediteur.id = :id THEN m.destinataire.id ELSE m.expediteur.id END " +
            "FROM Message m WHERE m.expediteur.id = :id OR m.destinataire.id = :id)")
    List<Utilisateur> findActiveContacts(@Param("id") Long id);
    @Query("SELECT m FROM Message m WHERE " +
            "(m.expediteur.id = :u1 AND m.destinataire.id = :u2) OR " +
            "(m.expediteur.id = :u2 AND m.destinataire.id = :u1) " +
            "ORDER BY m.timestamp ASC")
    List<Message> findConversation(@Param("u1") Long u1, @Param("u2") Long u2);
}