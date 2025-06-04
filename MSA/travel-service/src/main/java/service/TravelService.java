package service;

import model.Travel;
import model.CityVisit;
import repository.TravelRepository;
import repository.CityVisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * Service de gestion des voyages avec validation améliorée
 */
@Service
@Transactional
public class TravelService {

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private CityVisitRepository cityVisitRepository;

    /**
     * Récupère tous les voyages
     */
    @Transactional(readOnly = true)
    public List<Travel> getAllTravels() {
        return travelRepository.findAll();
    }

    /**
     * Récupère un voyage par son ID avec ses visites
     */
    @Transactional(readOnly = true)
    public Optional<Travel> getTravelById(Long id) {
        return travelRepository.findTravelWithCityVisits(id);
    }

    /**
     * Crée un nouveau voyage avec validations
     */
    public Travel createTravel(Travel travel) {
        validateTravel(travel);

        // Vérification d'unicité du nom
        if (travelRepository.findByTravelNameIgnoreCase(travel.getTravelName()).isPresent()) {
            throw new IllegalArgumentException("A travel with name '" + travel.getTravelName() + "' already exists");
        }

        return travelRepository.save(travel);
    }

    /**
     * Met à jour un voyage
     */
    public Travel updateTravel(Long id, Travel travelUpdate) {
        Optional<Travel> existingTravel = travelRepository.findById(id);
        if (existingTravel.isEmpty()) {
            throw new IllegalArgumentException("Travel with ID " + id + " not found");
        }

        validateTravel(travelUpdate);

        Travel travel = existingTravel.get();

        // Vérification d'unicité du nom si changé
        if (!travel.getTravelName().equalsIgnoreCase(travelUpdate.getTravelName())) {
            if (travelRepository.findByTravelNameIgnoreCase(travelUpdate.getTravelName()).isPresent()) {
                throw new IllegalArgumentException("A travel with name '" + travelUpdate.getTravelName() + "' already exists");
            }
        }

        travel.setTravelName(travelUpdate.getTravelName());
        travel.setStartDate(travelUpdate.getStartDate());
        travel.setEndDate(travelUpdate.getEndDate());
        travel.setDescription(travelUpdate.getDescription());

        return travelRepository.save(travel);
    }

    /**
     * Supprime un voyage et ses visites
     */
    public void deleteTravel(Long id) {
        if (!travelRepository.existsById(id)) {
            throw new IllegalArgumentException("Travel with ID " + id + " not found");
        }

        // Supprimer d'abord les visites associées
        List<CityVisit> visits = cityVisitRepository.findVisitsByTravelId(id);
        cityVisitRepository.deleteAll(visits);

        // Puis supprimer le voyage
        travelRepository.deleteById(id);
    }

    /**
     * Ajoute une visite de ville à un voyage avec validations
     */
    public CityVisit addCityVisitToTravel(Long travelId, CityVisit cityVisit) {
        Optional<Travel> travelOpt = travelRepository.findById(travelId);
        if (travelOpt.isEmpty()) {
            throw new IllegalArgumentException("Travel with ID " + travelId + " not found");
        }

        Travel travel = travelOpt.get();
        validateCityVisit(cityVisit, travel);

        // Auto-calcul du numéro de jour si pas spécifié
        if (cityVisit.getDayNumber() == null) {
            List<CityVisit> existingVisits = cityVisitRepository.findVisitsByTravelId(travelId);
            int nextDayNumber = existingVisits.size() + 1;
            cityVisit.setDayNumber(nextDayNumber);
        }

        // Calculer la date de visite basée sur le jour du voyage
        LocalDate visitDate = travel.getStartDate().plusDays(cityVisit.getDayNumber() - 1);
        cityVisit.setVisitDate(visitDate);
        cityVisit.setTravel(travel);

        return cityVisitRepository.save(cityVisit);
    }

    /**
     * Récupère les voyages qui visitent une ville
     */
    @Transactional(readOnly = true)
    public List<Travel> getTravelsByCityVisited(String cityName) {
        return travelRepository.findTravelsByCityVisited(cityName);
    }

    /**
     * Trouve les villes possibles à visiter entre deux villes
     */
    @Transactional(readOnly = true)
    public List<String> findIntermediateCities(String startCity, String endCity) {
        if (startCity.equalsIgnoreCase(endCity)) {
            throw new IllegalArgumentException("Start city and end city cannot be the same");
        }
        return travelRepository.findIntermediateCities(startCity, endCity);
    }

    /**
     * Récupère les visites d'un voyage triées par jour
     */
    @Transactional(readOnly = true)
    public List<CityVisit> getCityVisitsByTravel(Long travelId) {
        return cityVisitRepository.findVisitsByTravelId(travelId);
    }

    /**
     * Recherche par nom de voyage
     */
    @Transactional(readOnly = true)
    public Optional<Travel> getTravelByName(String travelName) {
        return travelRepository.findByTravelNameIgnoreCase(travelName);
    }

    /**
     * Récupère les voyages actifs
     */
    @Transactional(readOnly = true)
    public List<Travel> getActiveTravels() {
        return travelRepository.findActiveTravels();
    }

    /**
     * Statistiques des villes les plus visitées
     */
    @Transactional(readOnly = true)
    public List<TravelRepository.CityVisitStat> getMostVisitedCities() {
        return travelRepository.getMostVisitedCities();
    }

    // Méthodes de validation privées

    private void validateTravel(Travel travel) {
        if (travel.getStartDate() == null) {
            throw new IllegalArgumentException("Start date is required");
        }
        if (travel.getEndDate() == null) {
            throw new IllegalArgumentException("End date is required");
        }
        if (travel.getStartDate().isAfter(travel.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }
        if (travel.getStartDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }

        // Vérifier que le voyage ne dépasse pas 365 jours
        long daysBetween = ChronoUnit.DAYS.between(travel.getStartDate(), travel.getEndDate());
        if (daysBetween > 365) {
            throw new IllegalArgumentException("Travel duration cannot exceed 365 days");
        }
    }

    private void validateCityVisit(CityVisit cityVisit, Travel travel) {
        if (cityVisit.getCityName() == null || cityVisit.getCityName().trim().isEmpty()) {
            throw new IllegalArgumentException("City name is required");
        }

        if (cityVisit.getDayNumber() != null) {
            long travelDuration = ChronoUnit.DAYS.between(travel.getStartDate(), travel.getEndDate()) + 1;
            if (cityVisit.getDayNumber() < 1 || cityVisit.getDayNumber() > travelDuration) {
                throw new IllegalArgumentException("Day number must be between 1 and " + travelDuration);
            }
        }
    }
}