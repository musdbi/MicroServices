package service;

import model.Activity;
import dto.ActivityDto;
import repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${services.city-service.url:http://localhost:8081}")
    private String cityServiceUrl;

    private void validateCityExists(String cityName) {
        if (cityName == null || cityName.isEmpty()) {
            return;
        }

        try {
            String url = cityServiceUrl + "/api/cities/search?name=" + cityName;
            Map<String, Object> city = restTemplate.getForObject(url, Map.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("Ville '" + cityName + "' introuvable dans le système");
        } catch (Exception e) {
            throw new RuntimeException("Impossible de vérifier l'existence de la ville: " + e.getMessage());
        }
    }

    // Modifie la méthode createActivity
    public Activity createActivity(ActivityDto activityDto) {
        // Vérifier si l'activité existe déjà
        if (activityRepository.existsByNameIgnoreCaseAndCityNameIgnoreCase(
                activityDto.getName(), activityDto.getCityName())) {
            throw new RuntimeException("Activity already exists in this city");
        }

        // VALIDATION : Vérifier que la ville existe
        validateCityExists(activityDto.getCityName());

        // Convertir DTO vers entité
        Activity activity = new Activity(
                activityDto.getName(),
                activityDto.getCityName(),
                activityDto.getCityId(),
                activityDto.getPointOfInterestIds(),
                activityDto.getType(),
                activityDto.getDescription(),
                activityDto.getPrice(),
                activityDto.getDurationMinutes(),
                activityDto.getAvailableMonths(),
                activityDto.getDepartureLocation(),
                activityDto.getGeographicInfo()
        );

        // Définir les dates spécifiques si présentes
        activity.setStartDate(activityDto.getStartDate());
        activity.setEndDate(activityDto.getEndDate());

        return activityRepository.save(activity);
    }

    // Récupérer toutes les activités
    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }

    // Récupérer une activité par ID
    public Optional<Activity> getActivityById(String id) {
        return activityRepository.findById(id);
    }

    // Récupérer les activités par ville (nom)
    public List<Activity> getActivitiesByCity(String cityName) {
        return activityRepository.findByCityNameIgnoreCase(cityName);
    }

    // Récupérer les activités par ville (ID)
    public List<Activity> getActivitiesByCityId(Long cityId) {
        return activityRepository.findByCityId(cityId);
    }

    // Récupérer les activités par type
    public List<Activity> getActivitiesByType(String type) {
        return activityRepository.findByTypeIgnoreCase(type);
    }

    // REQUÊTE NOSQL : Activités associées à un POI donné
    public List<Activity> getActivitiesByPointOfInterest(String pointOfInterestId) {
        return activityRepository.findByPointOfInterestIdsContaining(pointOfInterestId);
    }

    // REQUÊTE NOSQL : Activités entre avril et juin (mois 4, 5, 6)
    public List<Activity> getActivitiesBetweenAprilAndJune() {
        List<Integer> months = Arrays.asList(4, 5, 6); // Avril, Mai, Juin
        return activityRepository.findByAvailableMonthsIn(months);
    }

    // Activités disponibles dans des mois spécifiques
    public List<Activity> getActivitiesByMonths(List<Integer> months) {
        return activityRepository.findByAvailableMonthsIn(months);
    }

    // Activités disponibles sur une période
    public List<Activity> getActivitiesAvailableBetween(LocalDate startDate, LocalDate endDate) {
        // Extraire les mois de la période
        List<Integer> months = new ArrayList<>();
        for (int month = startDate.getMonthValue(); month <= endDate.getMonthValue(); month++) {
            months.add(month);
        }
        return activityRepository.findActivitiesAvailableBetween(startDate, endDate, months);
    }

    // Rechercher par nom (autocomplétion)
    public List<Activity> searchActivitiesByName(String name) {
        return activityRepository.findByNameContainingIgnoreCase(name);
    }

    // Rechercher par gamme de prix
    public List<Activity> getActivitiesByPriceRange(Double minPrice, Double maxPrice) {
        return activityRepository.findByPriceBetween(minPrice, maxPrice);
    }

    // Rechercher par durée
    public List<Activity> getActivitiesByDuration(Integer minDuration, Integer maxDuration) {
        return activityRepository.findByDurationMinutesBetween(minDuration, maxDuration);
    }

    // Activités gratuites
    public List<Activity> getFreeActivities() {
        return activityRepository.findByPriceEquals(0.0);
    }

    // Activités avec plusieurs POI (circuits, tours)
    public List<Activity> getMultiPOIActivities(int minPOIs) {
        return activityRepository.findActivitiesWithMinimumPOIs(minPOIs);
    }

    // Activités par ville et prix
    public List<Activity> getActivitiesByCityAndPriceRange(String cityName, Double minPrice, Double maxPrice) {
        return activityRepository.findByCityNameIgnoreCaseAndPriceBetween(cityName, minPrice, maxPrice);
    }

    // Ajouter un POI à une activité
    public Activity addPointOfInterestToActivity(String activityId, String pointOfInterestId) {
        return activityRepository.findById(activityId).map(activity -> {
            if (activity.getPointOfInterestIds() == null) {
                activity.setPointOfInterestIds(new ArrayList<>());
            }
            if (!activity.getPointOfInterestIds().contains(pointOfInterestId)) {
                activity.getPointOfInterestIds().add(pointOfInterestId);
            }
            return activityRepository.save(activity);
        }).orElseThrow(() -> new RuntimeException("Activity not found with id: " + activityId));
    }

    // Retirer un POI d'une activité
    public Activity removePointOfInterestFromActivity(String activityId, String pointOfInterestId) {
        return activityRepository.findById(activityId).map(activity -> {
            if (activity.getPointOfInterestIds() != null) {
                activity.getPointOfInterestIds().remove(pointOfInterestId);
            }
            return activityRepository.save(activity);
        }).orElseThrow(() -> new RuntimeException("Activity not found with id: " + activityId));
    }

    // Mettre à jour une activité
    public Activity updateActivity(String id, ActivityDto activityDto) {
        return activityRepository.findById(id).map(existingActivity -> {
            existingActivity.setName(activityDto.getName());
            existingActivity.setCityName(activityDto.getCityName());
            existingActivity.setCityId(activityDto.getCityId());
            existingActivity.setPointOfInterestIds(activityDto.getPointOfInterestIds());
            existingActivity.setType(activityDto.getType());
            existingActivity.setDescription(activityDto.getDescription());
            existingActivity.setPrice(activityDto.getPrice());
            existingActivity.setDurationMinutes(activityDto.getDurationMinutes());
            existingActivity.setAvailableMonths(activityDto.getAvailableMonths());
            existingActivity.setStartDate(activityDto.getStartDate());
            existingActivity.setEndDate(activityDto.getEndDate());
            existingActivity.setDepartureLocation(activityDto.getDepartureLocation());
            existingActivity.setGeographicInfo(activityDto.getGeographicInfo());
            return activityRepository.save(existingActivity);
        }).orElseThrow(() -> new RuntimeException("Activity not found with id: " + id));
    }

    // Supprimer une activité
    public void deleteActivity(String id) {
        if (!activityRepository.existsById(id)) {
            throw new RuntimeException("Activity not found with id: " + id);
        }
        activityRepository.deleteById(id);
    }

    // Convertir entité vers DTO
    public ActivityDto convertToDto(Activity activity) {
        ActivityDto dto = new ActivityDto();
        dto.setId(activity.getId());
        dto.setName(activity.getName());
        dto.setCityName(activity.getCityName());
        dto.setCityId(activity.getCityId());
        dto.setPointOfInterestIds(activity.getPointOfInterestIds());
        dto.setType(activity.getType());
        dto.setDescription(activity.getDescription());
        dto.setPrice(activity.getPrice());
        dto.setDurationMinutes(activity.getDurationMinutes());
        dto.setAvailableMonths(activity.getAvailableMonths());
        dto.setStartDate(activity.getStartDate());
        dto.setEndDate(activity.getEndDate());
        dto.setDepartureLocation(activity.getDepartureLocation());
        dto.setGeographicInfo(activity.getGeographicInfo());
        return dto;
    }
}