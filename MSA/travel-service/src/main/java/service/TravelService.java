package service;

import model.Travel;
import model.TravelDay;
import repository.TravelRepository;
import repository.TravelDayRepository;
import dto.TravelDto;
import dto.TravelDayDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Map;

@Service
public class TravelService {

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private TravelDayRepository travelDayRepository;

    @Autowired
    private RestTemplate restTemplate;

    // URLs des autres microservices
    @Value("${services.city-service.url:http://localhost:8081}")
    private String cityServiceUrl;

    @Value("${services.tourism-service.url:http://localhost:8082}")
    private String tourismServiceUrl;

    // ===============================
    // MÉTHODES DE VALIDATION
    // ===============================

    /**
     * Vérifie qu'une ville existe via city-service
     */
    private void validateCityExists(String cityName) {
        if (cityName == null || cityName.isEmpty()) {
            return; // Pas de validation si pas de ville
        }

        try {
            String url = cityServiceUrl + "/api/cities/search?name=" + cityName;
            restTemplate.getForObject(url, Map.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("Ville '" + cityName + "' introuvable");
        } catch (Exception e) {
            throw new RuntimeException("Impossible de vérifier l'existence de la ville '" + cityName + "': " + e.getMessage());
        }
    }

    /**
     * Vérifie qu'un hébergement existe via tourism-service
     */
    private void validateAccommodationExists(String accommodationId, String expectedCityName) {
        if (accommodationId == null || accommodationId.isEmpty()) {
            return; // Pas de validation si pas d'hébergement
        }

        try {
            String url = tourismServiceUrl + "/api/tourism/accommodations/" + accommodationId;
            Map<String, Object> accommodation = restTemplate.getForObject(url, Map.class);

            // Vérifier que l'hébergement est bien dans la ville attendue
            if (accommodation != null && expectedCityName != null) {
                String accommodationCity = (String) accommodation.get("cityName");
                if (!expectedCityName.equalsIgnoreCase(accommodationCity)) {
                    throw new IllegalArgumentException(
                            "L'hébergement sélectionné est à " + accommodationCity +
                                    " mais vous avez indiqué " + expectedCityName
                    );
                }
            }
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("Hébergement avec ID '" + accommodationId + "' introuvable");
        } catch (IllegalArgumentException e) {
            throw e; // Re-lancer les erreurs de validation
        } catch (Exception e) {
            throw new RuntimeException("Impossible de vérifier l'existence de l'hébergement: " + e.getMessage());
        }
    }

    /**
     * Vérifie que les activités existent et récupère leurs villes
     */
    private List<String> validateAndGetActivityCities(List<String> activityIds) {
        List<String> cities = new ArrayList<>();

        if (activityIds == null || activityIds.isEmpty()) {
            return cities;
        }

        for (String activityId : activityIds) {
            try {
                String url = tourismServiceUrl + "/api/tourism/activities/" + activityId;
                Map<String, Object> activity = restTemplate.getForObject(url, Map.class);

                if (activity != null && activity.get("cityName") != null) {
                    cities.add((String) activity.get("cityName"));
                }
            } catch (HttpClientErrorException.NotFound e) {
                throw new IllegalArgumentException("Activité avec ID '" + activityId + "' introuvable");
            } catch (Exception e) {
                throw new RuntimeException("Impossible de vérifier l'existence de l'activité '" + activityId + "': " + e.getMessage());
            }
        }

        return cities;
    }

    // CRUD VOYAGES

    public List<Travel> getAllTravels() {
        return travelRepository.findAll();
    }

    public Optional<Travel> getTravelById(Long id) {
        return travelRepository.findTravelWithAllRelations(id);
    }

    public Travel createTravel(TravelDto travelDto) {
        if (travelDto.getStartDate().isAfter(travelDto.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        Travel travel = new Travel(
                travelDto.getTravelName(),
                travelDto.getStartDate(),
                travelDto.getEndDate(),
                travelDto.getDescription()
        );

        return travelRepository.save(travel);
    }

    public Travel updateTravel(Long id, TravelDto travelDto) {
        Travel existingTravel = travelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Travel with ID " + id + " not found"));

        if (travelDto.getStartDate().isAfter(travelDto.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        existingTravel.setTravelName(travelDto.getTravelName());
        existingTravel.setStartDate(travelDto.getStartDate());
        existingTravel.setEndDate(travelDto.getEndDate());
        existingTravel.setDescription(travelDto.getDescription());

        return travelRepository.save(existingTravel);
    }

    public void deleteTravel(Long id) {
        if (!travelRepository.existsById(id)) {
            throw new IllegalArgumentException("Travel with ID " + id + " not found");
        }
        travelRepository.deleteTravelWithCascade(id);
    }

    /**
     * MÉTHODE DE NETTOYAGE
     * Supprime les TravelDay orphelins (sans Travel parent) - OPTIONNEL MAIS UTILE
     */
    public void cleanupOrphanedTravelDays() {
        Long orphanedCount = travelRepository.countOrphanedTravelDays();

        if (orphanedCount > 0) {
            travelRepository.deleteOrphanedTravelDays();
        } else {
            System.out.println("Aucun TravelDay orphelin trouvé!");
        }
    }

    // GESTION DES JOURNÉES

    public List<TravelDay> getTravelDays(Long travelId) {
        if (!travelRepository.existsById(travelId)) {
            throw new IllegalArgumentException("Travel with ID " + travelId + " not found");
        }
        return travelDayRepository.findByTravelIdOrderByDayNumber(travelId);
    }

    public TravelDay addTravelDay(Long travelId, TravelDayDto dayDto) {
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new IllegalArgumentException("Travel with ID " + travelId + " not found"));

        // Vérification que la date est dans la période du voyage
        if (dayDto.getDate().isBefore(travel.getStartDate()) || dayDto.getDate().isAfter(travel.getEndDate())) {
            throw new IllegalArgumentException("La date doit être comprise entre le début et la fin du voyage");
        }

        boolean isLastDay = dayDto.getDate().equals(travel.getEndDate());

        // Pas d'hébergement le dernier jour
        if (isLastDay && dayDto.getAccommodationId() != null) {
            dayDto.setAccommodationId(null);
            dayDto.setAccommodationCityName(null);
            dayDto.setAccommodationCityId(null);
        }

        // Hébergement obligatoire sauf dernier jour
        if (!isLastDay && (dayDto.getAccommodationId() == null || dayDto.getAccommodationId().isEmpty())) {
            throw new IllegalArgumentException("Hébergement obligatoire sauf le dernier jour");
        }

        // Vérifier que la ville d'hébergement existe
        if (!isLastDay) {
            validateCityExists(dayDto.getAccommodationCityName());
            validateAccommodationExists(dayDto.getAccommodationId(), dayDto.getAccommodationCityName());
        }

        // Vérifier que toutes les activités existent
        List<String> activityCities = validateAndGetActivityCities(dayDto.getPlannedActivityIds());

        // Auto-calcul du numéro de jour si pas spécifié
        if (dayDto.getDayNumber() == null) {
            List<TravelDay> existingDays = travelDayRepository.findByTravelIdOrderByDayNumber(travelId);
            int nextDayNumber = existingDays.size() + 1;
            dayDto.setDayNumber(nextDayNumber);
        }

        Double calculatedBudget = calculateDailyBudget(dayDto.getPlannedActivityIds());

        Double finalBudget = dayDto.getDailyBudget() != null ? dayDto.getDailyBudget() : calculatedBudget;

        TravelDay travelDay = new TravelDay(dayDto.getDate(), dayDto.getDayNumber());

        // Définir l'hébergement si ce n'est pas le dernier jour
        if (!isLastDay) {
            travelDay.setAccommodationCityName(dayDto.getAccommodationCityName());
            travelDay.setAccommodationCityId(dayDto.getAccommodationCityId());
            travelDay.setAccommodationId(dayDto.getAccommodationId());
        }

        // Définir les activités planifiées
        travelDay.setPlannedActivityIds(dayDto.getPlannedActivityIds() != null ?
                dayDto.getPlannedActivityIds() : new ArrayList<>());
        travelDay.setDayDescription(dayDto.getDayDescription());
        travelDay.setDailyBudget(finalBudget);

        // Sauvegarder le TravelDay
        TravelDay savedDay = travelDayRepository.save(travelDay);

        // Créer la relation avec le Travel
        travelRepository.createHasDayRelation(travelId, savedDay.getId());

        return savedDay;
    }

    public TravelDay updateTravelDay(Long dayId, TravelDayDto dayDto) {
        TravelDay existingDay = travelDayRepository.findById(dayId)
                .orElseThrow(() -> new IllegalArgumentException("Travel day with ID " + dayId + " not found"));

        // VALIDATION : Vérifier que toutes les activités existent
        validateAndGetActivityCities(dayDto.getPlannedActivityIds());

        existingDay.setPlannedActivityIds(dayDto.getPlannedActivityIds());
        existingDay.setDayDescription(dayDto.getDayDescription());

        Double calculatedBudget = calculateDailyBudget(dayDto.getPlannedActivityIds());
        Double finalBudget = dayDto.getDailyBudget() != null ? dayDto.getDailyBudget() : calculatedBudget;

        existingDay.setDailyBudget(finalBudget);

        return travelDayRepository.save(existingDay);
    }

    public void deleteTravelDay(Long travelId, Long dayId) {
        if (!travelDayRepository.existsById(dayId)) {
            throw new IllegalArgumentException("Travel day with ID " + dayId + " not found");
        }
        travelDayRepository.deleteById(dayId);
    }

    /**
     * Calcule le budget quotidien en récupérant les prix des activités
     */
    public Double calculateDailyBudget(List<String> activityIds) {
        if (activityIds == null || activityIds.isEmpty()) {
            return 0.0;
        }

        double totalPrice = 0.0;

        for (String activityId : activityIds) {
            try {
                String url = tourismServiceUrl + "/api/tourism/activities/" + activityId;
                Map<String, Object> activity = restTemplate.getForObject(url, Map.class);

                if (activity != null && activity.get("price") != null) {
                    Double price = ((Number) activity.get("price")).doubleValue();
                    totalPrice += price;
                    System.out.println("Activité " + activityId + " - Prix: " + price + "€");
                }
            } catch (HttpClientErrorException.NotFound e) {
                System.err.println("Activité " + activityId + " non trouvée");
                throw new IllegalArgumentException("Activité avec ID '" + activityId + "' introuvable");
            } catch (Exception e) {
                System.err.println("Erreur lors de la récupération du prix pour l'activité " + activityId + ": " + e.getMessage());
                throw new RuntimeException("Impossible de calculer le budget: " + e.getMessage());
            }
        }

        return Math.round(totalPrice * 100.0) / 100.0;
    }

    /**
     * Méthode pour recalculer le budget d'une journée existante
     */
    public TravelDay recalculateDailyBudget(Long dayId) {
        TravelDay existingDay = travelDayRepository.findById(dayId)
                .orElseThrow(() -> new IllegalArgumentException("Travel day with ID " + dayId + " not found"));

        Double newBudget = calculateDailyBudget(existingDay.getPlannedActivityIds());
        existingDay.setDailyBudget(newBudget);

        return travelDayRepository.save(existingDay);
    }

    // REQUÊTES NOSQL

    public List<String> findIntermediateCities(String startCity, String endCity) {
        // Valider que les villes de départ et d'arrivée existent
        validateCityExists(startCity);
        validateCityExists(endCity);

        return travelRepository.findIntermediateCities(startCity, endCity);
    }

    public List<Travel> getTravelsByCityVisited(String cityName) {
        // Valider que la ville existe
        validateCityExists(cityName);

        return travelRepository.findTravelsByCityVisited(cityName);
    }

    // CONVERSIONS DTO

    public TravelDto convertToDto(Travel travel) {
        TravelDto dto = new TravelDto();
        dto.setId(travel.getId());
        dto.setTravelName(travel.getTravelName());
        dto.setStartDate(travel.getStartDate());
        dto.setEndDate(travel.getEndDate());
        dto.setDescription(travel.getDescription());
        return dto;
    }

    public TravelDayDto convertTravelDayToDto(TravelDay travelDay) {
        TravelDayDto dto = new TravelDayDto();
        dto.setId(travelDay.getId());
        dto.setDate(travelDay.getDate());
        dto.setDayNumber(travelDay.getDayNumber());
        dto.setAccommodationCityName(travelDay.getAccommodationCityName());
        dto.setAccommodationCityId(travelDay.getAccommodationCityId());
        dto.setAccommodationId(travelDay.getAccommodationId());
        dto.setPlannedActivityIds(travelDay.getPlannedActivityIds());
        dto.setDayDescription(travelDay.getDayDescription());
        dto.setDailyBudget(travelDay.getDailyBudget());
        return dto;
    }
}