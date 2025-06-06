package controller;

import model.Activity;
import dto.ActivityDto;
import service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

@RestController
@RequestMapping("/api/tourism/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    // Créer une activité
    @PostMapping
    public ResponseEntity<ActivityDto> createActivity(@Valid @RequestBody ActivityDto activityDto) {
        try {
            Activity createdActivity = activityService.createActivity(activityDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(activityService.convertToDto(createdActivity));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    // Récupérer toutes les activités
    @GetMapping
    public ResponseEntity<List<ActivityDto>> getAllActivities() {
        List<Activity> activities = activityService.getAllActivities();
        List<ActivityDto> activityDtos = activities.stream()
                .map(activityService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(activityDtos);
    }

    // Récupérer une activité par ID
    @GetMapping("/{id}")
    public ResponseEntity<ActivityDto> getActivityById(@PathVariable String id) {
        return activityService.getActivityById(id)
                .map(activity -> ResponseEntity.ok(activityService.convertToDto(activity)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Récupérer les activités par ville
    @GetMapping("/city/{cityName}")
    public ResponseEntity<List<ActivityDto>> getActivitiesByCity(@PathVariable String cityName) {
        List<Activity> activities = activityService.getActivitiesByCity(cityName);
        List<ActivityDto> activityDtos = activities.stream()
                .map(activityService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(activityDtos);
    }

    // REQUÊTE NOSQL : Activités associées à un POI donné
    @GetMapping("/poi/{pointOfInterestId}")
    public ResponseEntity<List<ActivityDto>> getActivitiesByPointOfInterest(@PathVariable String pointOfInterestId) {
        List<Activity> activities = activityService.getActivitiesByPointOfInterest(pointOfInterestId);
        List<ActivityDto> activityDtos = activities.stream()
                .map(activityService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(activityDtos);
    }

    // REQUÊTE NOSQL : Activités entre avril et juin
    @GetMapping("/april-to-june")
    public ResponseEntity<List<ActivityDto>> getActivitiesBetweenAprilAndJune() {
        List<Activity> activities = activityService.getActivitiesBetweenAprilAndJune();
        List<ActivityDto> activityDtos = activities.stream()
                .map(activityService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(activityDtos);
    }

    // Activités disponibles dans des mois spécifiques
    @GetMapping("/months")
    public ResponseEntity<List<ActivityDto>> getActivitiesByMonths(@RequestParam List<Integer> months) {
        List<Activity> activities = activityService.getActivitiesByMonths(months);
        List<ActivityDto> activityDtos = activities.stream()
                .map(activityService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(activityDtos);
    }

    // Activités disponibles sur une période
    @GetMapping("/period")
    public ResponseEntity<List<ActivityDto>> getActivitiesAvailableBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Activity> activities = activityService.getActivitiesAvailableBetween(startDate, endDate);
        List<ActivityDto> activityDtos = activities.stream()
                .map(activityService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(activityDtos);
    }

    // Rechercher par nom (autocomplétion)
    @GetMapping("/search")
    public ResponseEntity<List<ActivityDto>> searchActivitiesByName(@RequestParam String name) {
        List<Activity> activities = activityService.searchActivitiesByName(name);
        List<ActivityDto> activityDtos = activities.stream()
                .map(activityService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(activityDtos);
    }

    // Rechercher par gamme de prix
    @GetMapping("/price-range")
    public ResponseEntity<List<ActivityDto>> getActivitiesByPriceRange(
            @RequestParam Double minPrice, @RequestParam Double maxPrice) {
        List<Activity> activities = activityService.getActivitiesByPriceRange(minPrice, maxPrice);
        List<ActivityDto> activityDtos = activities.stream()
                .map(activityService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(activityDtos);
    }

    // Rechercher par durée
    @GetMapping("/duration")
    public ResponseEntity<List<ActivityDto>> getActivitiesByDuration(
            @RequestParam Integer minDuration, @RequestParam Integer maxDuration) {
        List<Activity> activities = activityService.getActivitiesByDuration(minDuration, maxDuration);
        List<ActivityDto> activityDtos = activities.stream()
                .map(activityService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(activityDtos);
    }

    // Activités gratuites
    @GetMapping("/free")
    public ResponseEntity<List<ActivityDto>> getFreeActivities() {
        List<Activity> activities = activityService.getFreeActivities();
        List<ActivityDto> activityDtos = activities.stream()
                .map(activityService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(activityDtos);
    }

    // Activités avec plusieurs POI (circuits, tours)
    @GetMapping("/multi-poi/{minPOIs}")
    public ResponseEntity<List<ActivityDto>> getMultiPOIActivities(@PathVariable int minPOIs) {
        List<Activity> activities = activityService.getMultiPOIActivities(minPOIs);
        List<ActivityDto> activityDtos = activities.stream()
                .map(activityService::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(activityDtos);
    }

    // Ajouter un POI à une activité
    @PostMapping("/{activityId}/poi/{pointOfInterestId}")
    public ResponseEntity<ActivityDto> addPointOfInterestToActivity(
            @PathVariable String activityId, @PathVariable String pointOfInterestId) {
        try {
            Activity updatedActivity = activityService.addPointOfInterestToActivity(activityId, pointOfInterestId);
            return ResponseEntity.ok(activityService.convertToDto(updatedActivity));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Retirer un POI d'une activité
    @DeleteMapping("/{activityId}/poi/{pointOfInterestId}")
    public ResponseEntity<ActivityDto> removePointOfInterestFromActivity(
            @PathVariable String activityId, @PathVariable String pointOfInterestId) {
        try {
            Activity updatedActivity = activityService.removePointOfInterestFromActivity(activityId, pointOfInterestId);
            return ResponseEntity.ok(activityService.convertToDto(updatedActivity));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Mettre à jour une activité
    @PutMapping("/{id}")
    public ResponseEntity<ActivityDto> updateActivity(
            @PathVariable String id, @Valid @RequestBody ActivityDto activityDto) {
        try {
            Activity updatedActivity = activityService.updateActivity(id, activityDto);
            return ResponseEntity.ok(activityService.convertToDto(updatedActivity));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer une activité
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable String id) {
        try {
            activityService.deleteActivity(id);
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