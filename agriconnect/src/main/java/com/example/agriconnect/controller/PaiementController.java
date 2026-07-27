package com.example.agriconnect.controller;

import com.example.agriconnect.classes.Offre;
import com.example.agriconnect.classes.Transaction;
import com.example.agriconnect.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/paiement") // <--- Vérifie l'orthographe exacte ici
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
                "https://agrilinkbycam.netlify.app/"

        },
        allowCredentials = "true"
)
public class PaiementController {

    @Autowired
    private TransactionRepository transactionRepo;
    @Autowired private ColisRepository colisRepo;
    @Autowired private OffreRepository offreRepo;
    @Autowired private AcheteurRepository acheteurRepo;
    @Autowired private VehiculeRepository vehiculeRepo;
    @Autowired private TransporteurRepository transporteurRepo;
    @Autowired private RestTemplate restTemplate;

    private static final String MONETBIL_KEY = "dPM0OxA4pmRnULfcL1Ci4myL3SEtfiq3";
    private static final String NUMERO_MARCHAND = "672017303";

    // ── INITIER ──────────────────────────────────────────────
    @PostMapping("/initier")
    public ResponseEntity<?> initierPaiement(@RequestBody Map<String, Object> body) throws JsonProcessingException {

        // 1. Créer la transaction en PENDING
        Transaction tx = new Transaction();
        tx.setStatut("PENDING");
        tx.setDate(LocalDate.now());
        tx.setMontantTotal(Double.valueOf(body.get("montant").toString()));
        tx.setVilleDepart(getOffre(body).getLieuProduction());
        tx.setVilleArrivee(body.get("villeDestination").toString());

        // Acheteur
        Long acheteurId = Long.valueOf(body.get("acheteurId").toString());
        tx.setAcheteur(acheteurRepo.findById(acheteurId).orElseThrow());

        // Véhicule
        if (body.get("vehiculeId") != null) {
            Long vehiculeId = Long.valueOf(body.get("vehiculeId").toString());
            tx.setVehicule(vehiculeRepo.findById(vehiculeId).orElseThrow());
        }

        // Transporteur
        if (body.get("transporteurId") != null) {
            Long transporteurId = Long.valueOf(body.get("transporteurId").toString());
            tx.setTransporteur(transporteurRepo.findById(transporteurId).orElseThrow());
        }

        // Date de départ choisie par l'acheteur
        String dateDepart = body.get("dateDepartChoisie").toString();
        tx.setReference("TX-" + System.currentTimeMillis());

        Transaction txSauvee = transactionRepo.save(tx);

        // Sauvegarder les infos de commande en session/DB pour finaliser après retour
        // (ici on les met dans la référence — en prod utiliser Redis ou table temporaire)
        // Pour simplifier : on stocke dans les notes de la transaction
        // On utilisera le transactionId dans le callback

        // 2. Appel API Monetbil
        String callbackUrl = "http://VOTRE_IP:8080/api/paiement/callback";
        String returnUrl   = "http://VOTRE_FRONTEND/paiement-retour";

        Map<String, String> monetbilPayload = new HashMap<>();
        monetbilPayload.put("service_key",   MONETBIL_KEY);
        monetbilPayload.put("amount",        tx.getMontantTotal().toString());
        monetbilPayload.put("phone",         body.get("telephone").toString());
        monetbilPayload.put("phone_lock",    "1");
        monetbilPayload.put("locale",        "fr");
        monetbilPayload.put("operator",      body.get("operateur").toString());
        monetbilPayload.put("return_url",    returnUrl);
        monetbilPayload.put("notify_url",    callbackUrl);
        monetbilPayload.put("payment_ref",   txSauvee.getId().toString());
        monetbilPayload.put("item_ref",      txSauvee.getId().toString());
        monetbilPayload.put("first_name",    body.getOrDefault("nomAcheteur", "").toString());
        monetbilPayload.put("email",         body.getOrDefault("emailAcheteur", "").toString());
        monetbilPayload.put("no_return_msg", "1");

        // Stocker les infos de commande pour les utiliser au callback
        // En prod : utiliser Redis. Ici on utilise un champ libre de la transaction.
        // On stocke en JSON dans le champ reference
        String commandeJson = new ObjectMapper().writeValueAsString(body);
        tx.setReference(txSauvee.getId() + "|" + commandeJson.substring(0, Math.min(commandeJson.length(), 200)));
        transactionRepo.save(tx);
// 1. Remplace l'ancien bloc ResponseEntity<Map> par celui-ci (Ligne 95 environ) :
        ResponseEntity<String> monetbilResp = restTemplate.postForEntity(
                "https://api.monetbil.com/payment/v1/request",
                monetbilPayload,
                String.class
        );

// 2. Affiche le résultat dans ta console IntelliJ pour comprendre le problème
        System.out.println("DEBUG MONETBIL : " + monetbilResp.getBody());

// 3. Pour que le reste du code ne plante pas, tu peux transformer la String en Map
// via ObjectMapper ou simplement analyser le contenu.
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseBody = mapper.readValue(monetbilResp.getBody(), Map.class);

        Map<String, Object> result = new HashMap<>();
        result.put("payment_url",    responseBody.get("payment_url"));
        result.put("transactionId",  txSauvee.getId());

        return ResponseEntity.ok(result);
    }

    // ── CALLBACK MONETBIL ────────────────────────────────────
    @PostMapping("/callback")
    public ResponseEntity<String> callback(@RequestBody Map<String, Object> body) {
        String status    = body.getOrDefault("status", "").toString();
        String paymentId = body.getOrDefault("payment_ref", "").toString();

        if (paymentId.isBlank()) return ResponseEntity.badRequest().body("KO");

        // Extraire l'ID de transaction (avant le "|")
        Long txId = Long.parseLong(paymentId.split("\\|")[0]);
        Transaction tx = transactionRepo.findById(txId).orElse(null);
        if (tx == null) return ResponseEntity.ok("OK");

        if ("success".equalsIgnoreCase(status)) {
            tx.setStatut("SUCCESS");
            // Créer le colis ici si nécessaire
        } else {
            tx.setStatut("FAILED");
        }
        transactionRepo.save(tx);
        return ResponseEntity.ok("OK");
    }

    // ── FINALISER (appelé par Angular au retour) ─────────────
    @PostMapping("/finaliser")
    public ResponseEntity<?> finaliserApresPaiement(@RequestBody Map<String, Object> body) {
        String reference = body.get("reference").toString();

        // Vérifier le statut sur Monetbil
        Map<String, String> verif = new HashMap<>();
        verif.put("service_key", MONETBIL_KEY);
        verif.put("paymentId",   reference);

        ResponseEntity<Map> resp = restTemplate.postForEntity(
                "https://api.monetbil.com/payment/v1/check",
                verif, Map.class
        );

        String status = resp.getBody().getOrDefault("status", "FAILED").toString();

        if (!"success".equalsIgnoreCase(status)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Paiement non confirmé"));
        }

        // Retrouver la transaction par sa référence Monetbil
        // (dans un vrai projet : stocker la référence Monetbil dans la transaction)
        // Ici on utilise le pendingTransactionId envoyé par Angular
        String txIdStr = body.getOrDefault("transactionId", "").toString();
        if (!txIdStr.isBlank()) {
            Long txId = Long.parseLong(txIdStr);
            Transaction tx = transactionRepo.findById(txId).orElse(null);
            if (tx != null) {
                tx.setStatut("SUCCESS");
                transactionRepo.save(tx);

                return ResponseEntity.ok(Map.of(
                        "transactionId", tx.getId(),
                        "dateDepart",    tx.getDate().toString(),
                        "statut",        "SUCCESS"
                ));
            }
        }

        return ResponseEntity.ok(Map.of("statut", "SUCCESS"));
    }

    private Offre getOffre(Map<String, Object> body) {
        Long offreId = Long.valueOf(body.get("offreId").toString());
        return offreRepo.findById(offreId).orElseThrow();
    }
}