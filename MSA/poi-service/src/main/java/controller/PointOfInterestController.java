package fr.dauphine.miageif.msa.poiservice.controller;

import fr.dauphine.miageif.msa.poiservice.model.PointOfInterest;
import fr.dauphine.miageif.msa.poiservice.service.PointOfInterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Controller REST simple pour la gestion des POI
 */
@RestController
@RequestMapping("/api/poi")
@CrossOrigin(origins = "*")
public class PointOfInterestController {

    @Autowired
    private PointOfInterestService poiService;

    /**
     * GET /api/poi
     * Récupère tous les POI
     */
    @GetMapping
    public ResponseEntity<List<PointOfInterest>> getAllPOIs() {
        List<PointOfInterest> pois = poiService.getAllPOIs();
        return ResponseEntity.ok(pois);
    }

    /**
     * GET /api/poi/{id}
     * Récupère un POI par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PointOfInterest> getPOIById(@PathVariable String id) {
        Optional<PointOfInterest> poi = poiService.getPOIById(id);
        return poi.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/poi
     * Crée un nouveau POI
     */
    @PostMapping
    public ResponseEntity<PointOfInterest> createPOI(@Valid @RequestBody PointOfInterest poi) {
        PointOfInterest createdPOI = poiService.createPOI(poi);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPOI);
    }

    /**
     * PUT /api/poi/{id}
     * Met à jour un POI existant
     */
    @PutMapping("/{id}")
    public ResponseEntity<PointOfInterest> updatePOI(@PathVariable String id, @Valid @RequestBody PointOfInterest poi) {
        try {
            PointOfInterest updatedPOI = poiService.updatePOI(id, poi);
            return ResponseEntity.ok(updatedPOI);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/poi/{id}
     * Supprime un POI
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePOI(@PathVariable String id) {
        try {
            poiService.deletePOI(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/poi/city/{cityName}
     * Récupère tous les POI d'une ville
     */
    @GetMapping("/city/{cityName}")
    public ResponseEntity<List<PointOfInterest>> getPOIsByCity(@PathVariable String cityName) {
        List<PointOfInterest> pois = poiService.getPOIsByCity(cityName);
        return ResponseEntity.ok(pois);
    }

    /**
     * GET /api/poi/type/{type}
     * Récupère tous les POI d'un type donné
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<PointOfInterest>> getPOIsByType(@PathVariable String type) {
        List<PointOfInterest> pois = poiService.getPOIsByType(type);
        return ResponseEntity.ok(pois);
    }

    /**
     * GET /api/poi/accommodations/{cityName}
     * Récupère les hébergements d'une ville
     * Répond à la requête NoSQL : "Quels sont les hébergements d'une ville ?"
     */
    @GetMapping("/accommodations/{cityName}")
    public ResponseEntity<List<PointOfInterest>> getAccommodationsByCity(@PathVariable String cityName) {
        List<PointOfInterest> accommodations = poiService.getAccommodationsByCity(cityName);
        return ResponseEntity.ok(accommodations);
    }

    /**
     * GET /api/poi/activities/{cityName}
     * Récupère les activités d'une ville
     * Répond à la requête NoSQL : "Quelles sont les activités associées à un point d'intérêt donné ?"
     */
    @GetMapping("/activities/{cityName}")
    public ResponseEntity<List<PointOfInterest>> getActivitiesByCity(@PathVariable String cityName) {
        List<PointOfInterest> activities = poiService.getActivitiesByCity(cityName);
        return ResponseEntity.ok(activities);
    }

    /**
     * GET /api/poi/search?term={searchTerm}
     * Recherche de POI par nom
     */
    @GetMapping("/search")
    public ResponseEntity<List<PointOfInterest>> searchPOIs(@RequestParam String term) {
        List<PointOfInterest> pois = poiService.searchPOIs(term);
        return ResponseEntity.ok(pois);
    }

    /**
     * GET /api/poi/health
     * Endpoint de santé
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("POI Service is running!");
    }
}