package controller;

import model.Travel;
import model.CityVisit;
import service.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Controller REST simple pour la gestion des voyages
 */
@RestController
@RequestMapping("/api/travels")
@CrossOrigin(origins = "*")
public class TravelController {

    @Autowired
    private TravelService travelService;

    /**
     * GET /api/travels
     * Récupère tous les voyages
     */
    @GetMapping
    public ResponseEntity<List<Travel>> getAllTravels() {
        List<Travel> travels = travelService.getAllTravels();
        return ResponseEntity.ok(travels);
    }

    /**
     * GET /api/travels/{id}
     * Récupère un voyage par son ID avec ses visites
     */
    @GetMapping("/{id}")
    public ResponseEntity<Travel> getTravelById(@PathVariable Long id) {
        Optional<Travel> travel = travelService.getTravelById(id);
        return travel.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/travels
     * Crée un nouveau voyage
     */
    @PostMapping
    public ResponseEntity<Travel> createTravel(@Valid @RequestBody Travel travel) {
        try {
            Travel createdTravel = travelService.createTravel(travel);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTravel);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PUT /api/travels/{id}
     * Met à jour un voyage existant
     */
    @PutMapping("/{id}")
    public ResponseEntity<Travel> updateTravel(@PathVariable Long id, @Valid @RequestBody Travel travel) {
        try {
            Travel updatedTravel = travelService.updateTravel(id, travel);
            return ResponseEntity.ok(updatedTravel);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/travels/{id}
     * Supprime un voyage
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTravel(@PathVariable Long id) {
        try {
            travelService.deleteTravel(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * POST /api/travels/{id}/visits
     * Ajoute une visite de ville à un voyage
     */
    @PostMapping("/{id}/visits")
    public ResponseEntity<CityVisit> addCityVisitToTravel(@PathVariable Long id, @Valid @RequestBody CityVisit cityVisit) {
        try {
            CityVisit createdVisit = travelService.addCityVisitToTravel(id, cityVisit);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdVisit);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * GET /api/travels/{id}/visits
     * Récupère les visites d'un voyage
     */
    @GetMapping("/{id}/visits")
    public ResponseEntity<List<CityVisit>> getCityVisitsByTravel(@PathVariable Long id) {
        List<CityVisit> visits = travelService.getCityVisitsByTravel(id);
        return ResponseEntity.ok(visits);
    }

    /**
     * GET /api/travels/city/{cityName}
     * Récupère les voyages qui visitent une ville donnée
     */
    @GetMapping("/city/{cityName}")
    public ResponseEntity<List<Travel>> getTravelsByCityVisited(@PathVariable String cityName) {
        List<Travel> travels = travelService.getTravelsByCityVisited(cityName);
        return ResponseEntity.ok(travels);
    }

    /**
     * GET /api/travels/intermediate-cities?start={startCity}&end={endCity}
     * Trouve les villes possibles à visiter entre deux villes
     * Répond à la requête NoSQL : "Étant données une ville de départ et une ville d'arrivée,
     * quels sont les différentes villes possibles à visiter entre les 2 ?"
     */
    @GetMapping("/intermediate-cities")
    public ResponseEntity<List<String>> findIntermediateCities(
            @RequestParam String start,
            @RequestParam String end) {
        List<String> intermediateCities = travelService.findIntermediateCities(start, end);
        return ResponseEntity.ok(intermediateCities);
    }

    /**
     * GET /api/travels/search?name={travelName}
     * Recherche un voyage par nom
     */
    @GetMapping("/search")
    public ResponseEntity<Travel> getTravelByName(@RequestParam String name) {
        Optional<Travel> travel = travelService.getTravelByName(name);
        return travel.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/travels/health
     * Endpoint de santé
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Travel Service is running!");
    }
}