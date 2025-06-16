package controller;

import model.PointOfInterest;
import dto.PointOfInterestDto;
import service.PointOfInterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tourism/poi")
public class PointOfInterestController {

    @Autowired
    private PointOfInterestService poiService;

    // Créer un POI
    @PostMapping
    public ResponseEntity<PointOfInterestDto> createPointOfInterest(@Valid @RequestBody PointOfInterestDto poiDto) {
        try {
            PointOfInterest createdPoi = poiService.createPointOfInterest(poiDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(poiService.convertToDto(createdPoi));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    // Récupérer tous les POI
    @GetMapping
    public ResponseEntity<List<PointOfInterestDto>> getAllPointsOfInterest() {
        List<PointOfInterest> pois = poiService.getAllPointsOfInterest();
        List<PointOfInterestDto> poiDtos = pois.stream()
                .map(poiService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(poiDtos);
    }

    // Récupérer un POI par ID
    @GetMapping("/{id}")
    public ResponseEntity<PointOfInterestDto> getPointOfInterestById(@PathVariable String id) {
        return poiService.getPointOfInterestById(id)
                .map(poi -> ResponseEntity.ok(poiService.convertToDto(poi)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Récupérer les POI par le nom de la ville
    @GetMapping("/city/{cityName}")
    public ResponseEntity<List<PointOfInterestDto>> getPointsOfInterestByCity(@PathVariable String cityName) {
        List<PointOfInterest> pois = poiService.getPointsOfInterestByCity(cityName);
        List<PointOfInterestDto> poiDtos = pois.stream()
                .map(poiService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(poiDtos);
    }

    // Récupérer les POI par ID de ville
    @GetMapping("/city-id/{cityId}")
    public ResponseEntity<List<PointOfInterestDto>> getPointsOfInterestByCityId(@PathVariable Long cityId) {
        List<PointOfInterest> pois = poiService.getPointsOfInterestByCityId(cityId);
        List<PointOfInterestDto> poiDtos = pois.stream()
                .map(poiService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(poiDtos);
    }

    // Récupérer les POI par type
    @GetMapping("/type/{type}")
    public ResponseEntity<List<PointOfInterestDto>> getPointsOfInterestByType(@PathVariable String type) {
        List<PointOfInterest> pois = poiService.getPointsOfInterestByType(type);
        List<PointOfInterestDto> poiDtos = pois.stream()
                .map(poiService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(poiDtos);
    }

    // Récupérer les POI par ville et type
    @GetMapping("/city/{cityName}/type/{type}")
    public ResponseEntity<List<PointOfInterestDto>> getPointsOfInterestByCityAndType(
            @PathVariable String cityName, @PathVariable String type) {
        List<PointOfInterest> pois = poiService.getPointsOfInterestByCityAndType(cityName, type);
        List<PointOfInterestDto> poiDtos = pois.stream()
                .map(poiService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(poiDtos);
    }

    // Rechercher par nom
    @GetMapping("/search")
    public ResponseEntity<List<PointOfInterestDto>> searchPointsOfInterestByName(@RequestParam String name) {
        List<PointOfInterest> pois = poiService.searchPointsOfInterestByName(name);
        List<PointOfInterestDto> poiDtos = pois.stream()
                .map(poiService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(poiDtos);
    }

    // Mettre à jour un POI
    @PutMapping("/{id}")
    public ResponseEntity<PointOfInterestDto> updatePointOfInterest(
            @PathVariable String id, @Valid @RequestBody PointOfInterestDto poiDto) {
        try {
            PointOfInterest updatedPoi = poiService.updatePointOfInterest(id, poiDto);
            return ResponseEntity.ok(poiService.convertToDto(updatedPoi));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer un POI
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePointOfInterest(@PathVariable String id) {
        try {
            poiService.deletePointOfInterest(id);
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