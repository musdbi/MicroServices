package controller;

import model.Accommodation;
import dto.AccommodationDto;
import service.AccommodationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tourism/accommodations")
public class AccommodationController {

    @Autowired
    private AccommodationService accommodationService;

    // Créer un hébergement
    @PostMapping
    public ResponseEntity<AccommodationDto> createAccommodation(@Valid @RequestBody AccommodationDto accommodationDto) {
        try {
            Accommodation createdAccommodation = accommodationService.createAccommodation(accommodationDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(accommodationService.convertToDto(createdAccommodation));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    // Récupérer tous les hébergements
    @GetMapping
    public ResponseEntity<List<AccommodationDto>> getAllAccommodations() {
        List<Accommodation> accommodations = accommodationService.getAllAccommodations();
        List<AccommodationDto> accommodationDtos = accommodations.stream()
                .map(accommodationService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(accommodationDtos);
    }

    // Récupérer un hébergement par ID
    @GetMapping("/{id}")
    public ResponseEntity<AccommodationDto> getAccommodationById(@PathVariable String id) {
        return accommodationService.getAccommodationById(id)
                .map(accommodation -> ResponseEntity.ok(accommodationService.convertToDto(accommodation)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Récupérer les hébergements par ville - REQUÊTE NOSQL IMPORTANTE
    @GetMapping("/city/{cityName}")
    public ResponseEntity<List<AccommodationDto>> getAccommodationsByCity(@PathVariable String cityName) {
        List<Accommodation> accommodations = accommodationService.getAccommodationsByCity(cityName);
        List<AccommodationDto> accommodationDtos = accommodations.stream()
                .map(accommodationService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(accommodationDtos);
    }

    // Récupérer les hébergements par ID de ville
    @GetMapping("/city-id/{cityId}")
    public ResponseEntity<List<AccommodationDto>> getAccommodationsByCityId(@PathVariable Long cityId) {
        List<Accommodation> accommodations = accommodationService.getAccommodationsByCityId(cityId);
        List<AccommodationDto> accommodationDtos = accommodations.stream()
                .map(accommodationService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(accommodationDtos);
    }

    // Récupérer les hébergements par type
    @GetMapping("/type/{type}")
    public ResponseEntity<List<AccommodationDto>> getAccommodationsByType(@PathVariable String type) {
        List<Accommodation> accommodations = accommodationService.getAccommodationsByType(type);
        List<AccommodationDto> accommodationDtos = accommodations.stream()
                .map(accommodationService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(accommodationDtos);
    }

    // Récupérer les hébergements par ville et type
    @GetMapping("/city/{cityName}/type/{type}")
    public ResponseEntity<List<AccommodationDto>> getAccommodationsByCityAndType(
            @PathVariable String cityName, @PathVariable String type) {
        List<Accommodation> accommodations = accommodationService.getAccommodationsByCityAndType(cityName, type);
        List<AccommodationDto> accommodationDtos = accommodations.stream()
                .map(accommodationService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(accommodationDtos);
    }

    // Rechercher par nom (autocomplétion)
    @GetMapping("/search")
    public ResponseEntity<List<AccommodationDto>> searchAccommodationsByName(@RequestParam String name) {
        List<Accommodation> accommodations = accommodationService.searchAccommodationsByName(name);
        List<AccommodationDto> accommodationDtos = accommodations.stream()
                .map(accommodationService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(accommodationDtos);
    }

    // Rechercher par gamme de prix
    @GetMapping("/price-range")
    public ResponseEntity<List<AccommodationDto>> getAccommodationsByPriceRange(
            @RequestParam Double minPrice, @RequestParam Double maxPrice) {
        List<Accommodation> accommodations = accommodationService.getAccommodationsByPriceRange(minPrice, maxPrice);
        List<AccommodationDto> accommodationDtos = accommodations.stream()
                .map(accommodationService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(accommodationDtos);
    }

    // Rechercher par ville et gamme de prix
    @GetMapping("/city/{cityName}/price-range")
    public ResponseEntity<List<AccommodationDto>> getAccommodationsByCityAndPriceRange(
            @PathVariable String cityName, @RequestParam Double minPrice, @RequestParam Double maxPrice) {
        List<Accommodation> accommodations = accommodationService.getAccommodationsByCityAndPriceRange(cityName, minPrice, maxPrice);
        List<AccommodationDto> accommodationDtos = accommodations.stream()
                .map(accommodationService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(accommodationDtos);
    }

    // Ajouter un avis
    @PostMapping("/{id}/reviews")
    public ResponseEntity<AccommodationDto> addReview(@PathVariable String id, @RequestBody String review) {
        try {
            Accommodation updatedAccommodation = accommodationService.addReview(id, review);
            return ResponseEntity.ok(accommodationService.convertToDto(updatedAccommodation));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Mettre à jour un hébergement
    @PutMapping("/{id}")
    public ResponseEntity<AccommodationDto> updateAccommodation(
            @PathVariable String id, @Valid @RequestBody AccommodationDto accommodationDto) {
        try {
            Accommodation updatedAccommodation = accommodationService.updateAccommodation(id, accommodationDto);
            return ResponseEntity.ok(accommodationService.convertToDto(updatedAccommodation));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer un hébergement
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccommodation(@PathVariable String id) {
        try {
            accommodationService.deleteAccommodation(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Service running: " + this.getClass().getSimpleName());
    }
}