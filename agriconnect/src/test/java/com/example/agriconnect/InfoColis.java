package com.example.agriconnect;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class InfoColis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String position;
    private double temperature;
    private double humidite;

    private LocalDateTime timestamp;

    @ManyToOne
    private Colis colis;
}
