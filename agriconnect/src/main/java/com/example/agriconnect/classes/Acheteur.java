package com.example.agriconnect.classes;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
@Table(name = "acheteurs")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Acheteur extends Utilisateur {

    private String adresseLivraison;

    // ✅ CORRECTION : @JsonManagedReference remplacé par @JsonIgnore
    // @JsonManagedReference exige un @JsonBackReference dans Transaction.java
    // qui est absent → Jackson refuse de désérialiser Acheteur en JSON
    // → Spring répond 415 sur le endpoint /login qui utilise @RequestBody
    // @JsonIgnore est plus simple : on ignore juste les transactions
    // lors de la sérialisation/désérialisation (évite aussi les boucles infinies)
    @JsonIgnore
    @OneToMany(mappedBy = "acheteur")
    private List<Transaction> transactions;
}