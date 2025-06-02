package service;

import model.Travel;
import model.CityVisit;
import repository.TravelRepository;
import repository.CityVisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service simple de gestion des voyages
 * Focus sur les fonctionnalités essentielles Neo4j
 */
@Service
public class TravelService {

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private CityVisitRepository cityVisitRepository;

    /**
     * Récupère tous les voyages
     */
    public List<Travel> getAllTravels() {
        return travelRepository.findAll();
    }

    /**
     * Récupère un voyage par son ID avec ses visites
     */
    public Optional<Travel> getTravelById(Long id) {
        return travelRepository.findTravelWithCityVisits(id);
    }

    /**
     * Crée un nouveau voyage
     */
    public Travel createTravel(Travel travel) {
        // Validation des dates
        if (travel.getStartDate().isAfter(travel.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
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

        Travel travel = existingTravel.get();
        travel.setTravelName(travelUpdate.getTravelName());
        travel.setStartDate(travelUpdate.getStartDate());
        travel.setEndDate(travelUpdate.getEndDate());
        travel.setDescription(travelUpdate.getDescription());

        return travelRepository.save(travel);
    }

    /**
     * Supprime un voyage
     */
    public void deleteTravel(Long id) {
        if (!travelRepository.existsById(id)) {
            throw new IllegalArgumentException("Travel with ID " + id + " not found");
        }
        travelRepository.deleteById(id);
    }

    /**
     * Ajoute une visite de ville à un voyage
     */
    public CityVisit addCityVisitToTravel(Long travelId, CityVisit cityVisit) {
        Optional<Travel> travel = travelRepository.findById(travelId);
        if (travel.isEmpty()) {
            throw new IllegalArgumentException("Travel with ID " + travelId + " not found");
        }

        // Auto-calcul du numéro de jour si pas spécifié
        if (cityVisit.getDayNumber() == null) {
            List<CityVisit> existingVisits = cityVisitRepository.findVisitsByTravelId(travelId);
            int nextDayNumber = existingVisits.size() + 1;
            cityVisit.setDayNumber(nextDayNumber);
        }

        return cityVisitRepository.save(cityVisit);
    }

    /**
     * Récupère les voyages qui visitent une ville
     */
    public List<Travel> getTravelsByCityVisited(String cityName) {
        return travelRepository.findTravelsByCityVisited(cityName);
    }

    /**
     * Trouve les villes possibles à visiter entre deux villes
     * Répond à la requête NoSQL principale du projet
     */
    public List<String> findIntermediateCities(String startCity, String endCity) {
        return travelRepository.findIntermediateCities(startCity, endCity);
    }

    /**
     * Récupère les visites d'un voyage
     */
    public List<CityVisit> getCityVisitsByTravel(Long travelId) {
        return cityVisitRepository.findVisitsByTravelId(travelId);
    }

    /**
     * Recherche par nom de voyage
     */
    public Optional<Travel> getTravelByName(String travelName) {
        return travelRepository.findByTravelNameIgnoreCase(travelName);
    }
}