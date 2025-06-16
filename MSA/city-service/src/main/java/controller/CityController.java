package controller;

import dto.CityDistanceDto;
import model.City;
import service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Controller REST pour la gestion des villes
 * Expose les API du microservice city-service
 */
@RestController
@RequestMapping("/api/cities")
public class CityController {

    @Autowired
    private CityService cityService;

    /**
     * GET /api/cities
     * Récupère toutes les villes
     */
    @GetMapping
    public ResponseEntity<List<City>> getAllCities() {
        List<City> cities = cityService.getAllCities();
        return ResponseEntity.ok(cities);
    }

    /**
     * GET /api/cities/{id}
     * Récupère une ville par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<City> getCityById(@PathVariable Long id) {
        Optional<City> city = cityService.getCityById(id);
        return city.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/cities/search?name={cityName}
     * Récupère une ville par son nom
     */
    @GetMapping("/search")
    public ResponseEntity<City> getCityByName(@RequestParam String name) {
        Optional<City> city = cityService.getCityByName(name);
        return city.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/cities
     * Crée une nouvelle ville
     */
    @PostMapping
    public ResponseEntity<City> createCity(@Valid @RequestBody City city) {
        try {
            City createdCity = cityService.createCity(city);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCity);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PUT /api/cities/{id}
     * Met à jour une ville existante
     */
    @PutMapping("/{id}")
    public ResponseEntity<City> updateCity(@PathVariable Long id, @Valid @RequestBody City city) {
        try {
            City updatedCity = cityService.updateCity(id, city);
            return ResponseEntity.ok(updatedCity);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/cities/{id}
     * Supprime une ville
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        try {
            cityService.deleteCity(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/cities/country/{country}
     * Récupère toutes les villes d'un pays
     */
    @GetMapping("/country/{country}")
    public ResponseEntity<List<City>> getCitiesByCountry(@PathVariable String country) {
        List<City> cities = cityService.getCitiesByCountry(country);
        return ResponseEntity.ok(cities);
    }

    /**
     * GET /api/cities/nearby?city={cityName}&radius={radiusKm}
     * Trouve les villes dans un rayon donné
     */
    @GetMapping("/nearby")
    public ResponseEntity<List<City>> getCitiesWithinRadius(
            @RequestParam String city,
            @RequestParam(defaultValue = "50.0") Double radius) {
        try {
            List<City> cities = cityService.getCitiesWithinRadius(city, radius);
            return ResponseEntity.ok(cities);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * GET /api/cities/distance?city1={cityName1}&city2={cityName2}
     * Calcule la distance et le temps de trajet entre deux villes
     */
    @GetMapping("/distance")
    public ResponseEntity<CityDistanceDto> calculateDistance(
            @RequestParam String city1,
            @RequestParam String city2) {
        try {
            CityDistanceDto distance = cityService.calculateDistanceBetweenCities(city1, city2);
            return ResponseEntity.ok(distance);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * GET /api/cities/health
     * Endpoint de santé pour vérifier que le service fonctionne
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("City Service is running!");
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Service running: " + this.getClass().getSimpleName());
    }
}