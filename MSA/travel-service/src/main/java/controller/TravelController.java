package controller;

import model.Travel;
import model.TravelDay;
import service.TravelService;
import dto.TravelDto;
import dto.TravelDayDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/travels")
@CrossOrigin(origins = "*")
public class TravelController {

    @Autowired
    private TravelService travelService;

    // ===============================
    // CRUD BASIQUE VOYAGES (inchangé)
    // ===============================

    @GetMapping
    public ResponseEntity<List<TravelDto>> getAllTravels() {
        List<Travel> travels = travelService.getAllTravels();
        List<TravelDto> travelDtos = travels.stream()
                .map(travelService::convertToDto)
                .toList();
        return ResponseEntity.ok(travelDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TravelDto> getTravelById(@PathVariable Long id) {
        Optional<Travel> travel = travelService.getTravelById(id);
        return travel.map(t -> ResponseEntity.ok(travelService.convertToDto(t)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TravelDto> createTravel(@Valid @RequestBody TravelDto travelDto) {
        try {
            Travel createdTravel = travelService.createTravel(travelDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(travelService.convertToDto(createdTravel));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TravelDto> updateTravel(@PathVariable Long id, @Valid @RequestBody TravelDto travelDto) {
        try {
            Travel updatedTravel = travelService.updateTravel(id, travelDto);
            return ResponseEntity.ok(travelService.convertToDto(updatedTravel));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTravel(@PathVariable Long id) {
        try {
            travelService.deleteTravel(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ===============================
    // GESTION DES JOURNÉES (simplifié)
    // ===============================

    @GetMapping("/{id}/days")
    public ResponseEntity<List<TravelDayDto>> getTravelDays(@PathVariable Long id) {
        List<TravelDay> days = travelService.getTravelDays(id);
        List<TravelDayDto> dayDtos = days.stream()
                .map(travelService::convertTravelDayToDto)
                .toList();
        return ResponseEntity.ok(dayDtos);
    }

    @PostMapping("/{id}/days")
    public ResponseEntity<TravelDayDto> addTravelDay(@PathVariable Long id, @Valid @RequestBody TravelDayDto dayDto) {
        try {
            TravelDay createdDay = travelService.addTravelDay(id, dayDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(travelService.convertTravelDayToDto(createdDay));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/days/{dayId}")
    public ResponseEntity<TravelDayDto> updateTravelDay(@PathVariable Long dayId, @Valid @RequestBody TravelDayDto dayDto) {
        try {
            TravelDay updatedDay = travelService.updateTravelDay(dayId, dayDto);
            return ResponseEntity.ok(travelService.convertTravelDayToDto(updatedDay));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{travelId}/days/{dayId}")
    public ResponseEntity<Void> deleteTravelDay(@PathVariable Long travelId, @PathVariable Long dayId) {
        try {
            travelService.deleteTravelDay(travelId, dayId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ===============================
    // REQUÊTE NOSQL PRINCIPALE
    // ===============================

    /**
     * Villes possibles à visiter entre deux villes
     * NOTE: Cette implémentation nécessitera d'appeler tourism-service
     * pour récupérer les villes des activités
     */
    @GetMapping("/intermediate-cities")
    public ResponseEntity<List<String>> findIntermediateCities(
            @RequestParam String start, @RequestParam String end) {
        List<String> intermediateCities = travelService.findIntermediateCities(start, end);
        return ResponseEntity.ok(intermediateCities);
    }

    @GetMapping("/city/{cityName}")
    public ResponseEntity<List<TravelDto>> getTravelsByCityVisited(@PathVariable String cityName) {
        List<Travel> travels = travelService.getTravelsByCityVisited(cityName);
        List<TravelDto> travelDtos = travels.stream()
                .map(travelService::convertToDto)
                .toList();
        return ResponseEntity.ok(travelDtos);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Travel Service is running! 🚀");
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Service running: " + this.getClass().getSimpleName());
    }
}