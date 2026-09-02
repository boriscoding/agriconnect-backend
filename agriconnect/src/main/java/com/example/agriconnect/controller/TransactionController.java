package com.example.agriconnect.controller;

import com.example.agriconnect.classes.Transaction;
import com.example.agriconnect.service.TransactionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions") // ✅ Ajout de /api pour correspondre à tes appels Angular
@CrossOrigin(
        origins = {
                "http://localhost:4200",
                "http://172.27.208.1:4200",
                "http://192.168.56.1:4200",
                "http://192.168.197.1:4200",
                "http://192.168.226.1:4200",
                "http://10.177.225.196:4200",
                "http://10.101.75.196:4200",
                "https://unsacked-improvisationally-suanne.ngrok-free.dev",
                "https://agrilinkbycam.netlify.app"

        },
        allowCredentials = "true"
)
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }



    @PostMapping
    public Transaction create(@RequestBody Transaction transaction) {
        return transactionService.create(transaction);
    }

    @GetMapping
    public List<Transaction> getAll() {
        return transactionService.findAll();
    }

    @GetMapping("/{id}")
    public Transaction getById(@PathVariable Long id) {
        return transactionService.findById(id);
    }

    @PostMapping("/payer-groupage")
    public Transaction payerGroupage(@RequestBody Map<String, Object> payload) {
        Long acheteurId = Long.valueOf(payload.get("acheteurId").toString());
        List<Long> colisIds = (List<Long>) payload.get("colisIds");
        String depart = payload.get("villeDepart").toString();
        String arrivee = payload.get("villeArrivee").toString();

        return transactionService.creerGroupage(acheteurId, colisIds, depart, arrivee);
    }
    @GetMapping("/trouver-trajets")
    public List<Transaction> trouverTrajets(
            @RequestParam String ville,
            @RequestParam Double poids) {
        return transactionService.rechercherTrajets(ville, poids);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        transactionService.delete(id);
    }
}