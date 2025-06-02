package service;

import model.PointOfInterest;
import repository.PointOfInterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service simple de gestion des Points d'Intérêt
 * Focus sur les fonctionnalités essentielles
 */
@Service
public class PointOfInterestService {

    @Autowired
    private PointOfInterestRepository poiRepository;

    /**
     * Récupère tous les POI
     */
    public List<PointOfInterest> getAllPOIs() {
        return poiRepository.findAll();
    }

    /**
     * Récupère un POI par son ID
     */
    public Optional<PointOfInterest> getPOIById(String id) {
        return poiRepository.findById(id);
    }

    /**
     * Crée un nouveau POI
     */
    public PointOfInterest createPOI(PointOfInterest poi) {
        return poiRepository.save(poi);
    }

    /**
     * Met à jour un POI existant
     */
    public PointOfInterest updatePOI(String id, PointOfInterest poiUpdate) {
        Optional<PointOfInterest> existingPOI = poiRepository.findById(id);
        if (existingPOI.isEmpty()) {
            throw new IllegalArgumentException("POI with ID " + id + " not found");
        }

        PointOfInterest poi = existingPOI.get();
        poi.setName(poiUpdate.getName());
        poi.setCityName(poiUpdate.getCityName());
        poi.setType(poiUpdate.getType());
        poi.setDescription(poiUpdate.getDescription());
        poi.setPrice(poiUpdate.getPrice());

        return poiRepository.save(poi);
    }

    /**
     * Supprime un POI
     */
    public void deletePOI(String id) {
        if (!poiRepository.existsById(id)) {
            throw new IllegalArgumentException("POI with ID " + id + " not found");
        }
        poiRepository.deleteById(id);
    }

    /**
     * Récupère les POI d'une ville
     */
    public List<PointOfInterest> getPOIsByCity(String cityName) {
        return poiRepository.findByCityNameIgnoreCase(cityName);
    }

    /**
     * Récupère les POI par type
     */
    public List<PointOfInterest> getPOIsByType(String type) {
        return poiRepository.findByType(type);
    }

    /**
     * Récupère les hébergements d'une ville
     * Répond à la requête NoSQL : "Quels sont les hébergements d'une ville ?"
     */
    public List<PointOfInterest> getAccommodationsByCity(String cityName) {
        return poiRepository.findByCityNameIgnoreCaseAndType(cityName, "hotel");
    }

    /**
     * Récupère les activités d'une ville (simulate)
     * Répond à la requête NoSQL : "Quelles sont les activités associées à un point d'intérêt donné ?"
     */
    public List<PointOfInterest> getActivitiesByCity(String cityName) {
        return poiRepository.findByCityNameIgnoreCaseAndType(cityName, "activity");
    }

    /**
     * Recherche de POI par nom partiel
     */
    public List<PointOfInterest> searchPOIs(String searchTerm) {
        return poiRepository.findByNameContainingIgnoreCase(searchTerm);
    }
}