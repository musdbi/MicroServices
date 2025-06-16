package service;

import dto.CityDistanceDto;
import model.City;
import repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service de gestion des villes
 * Contient la logique métier et les calculs de distance
 */
@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    /**
     * Récupère toutes les villes
     */
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    /**
     * Récupère une ville par son ID
     */
    public Optional<City> getCityById(Long id) {
        return cityRepository.findById(id);
    }

    /**
     * Récupère une ville par son nom
     */
    public Optional<City> getCityByName(String cityName) {
        return cityRepository.findByCityNameIgnoreCase(cityName);
    }

    /**
     * Crée une nouvelle ville
     */
    public City createCity(City city) {
        // Vérification que la ville n'existe pas déjà
        if (cityRepository.existsByCityNameIgnoreCase(city.getCityName())) {
            throw new IllegalArgumentException("City with name '" + city.getCityName() + "' already exists");
        }
        return cityRepository.save(city);
    }

    /**
     * Met à jour une ville existante
     */
    public City updateCity(Long id, City cityUpdate) {
        Optional<City> existingCity = cityRepository.findById(id);
        if (existingCity.isEmpty()) {
            throw new IllegalArgumentException("City with ID " + id + " not found");
        }

        City city = existingCity.get();
        city.setCityName(cityUpdate.getCityName());
        city.setGeographicInfo(cityUpdate.getGeographicInfo());

        return cityRepository.save(city);
    }

    /**
     * Supprime une ville
     */
    public void deleteCity(Long id) {
        if (!cityRepository.existsById(id)) {
            throw new IllegalArgumentException("City with ID " + id + " not found");
        }
        cityRepository.deleteById(id);
    }

    /**
     * Recherche les villes d'un pays
     */
    public List<City> getCitiesByCountry(String country) {
        return cityRepository.findByCountry(country);
    }

    /**
     * Recherche les villes dans un rayon donné
     */
    public List<City> getCitiesWithinRadius(String cityName, Double radiusKm) {
        Optional<City> referenceCity = getCityByName(cityName);
        if (referenceCity.isEmpty()) {
            throw new IllegalArgumentException("Reference city '" + cityName + "' not found");
        }

        City city = referenceCity.get();
        Double latitude = city.getGeographicInfo().getLatitude();
        Double longitude = city.getGeographicInfo().getLongitude();

        if (latitude == null || longitude == null) {
            throw new IllegalArgumentException("Reference city '" + cityName + "' has no geographic coordinates");
        }

        return cityRepository.findCitiesWithinRadius(latitude, longitude, radiusKm);
    }

    /**
     * Calcule la distance et le temps de trajet entre deux villes
     * Répond à la requête NoSQL : "Quel est le temps de trajet et la distance entre 2 villes données ?"
     */
    public CityDistanceDto calculateDistanceBetweenCities(String cityName1, String cityName2) {
        Optional<City> city1 = getCityByName(cityName1);
        Optional<City> city2 = getCityByName(cityName2);

        if (city1.isEmpty()) {
            throw new IllegalArgumentException("City '" + cityName1 + "' not found");
        }
        if (city2.isEmpty()) {
            throw new IllegalArgumentException("City '" + cityName2 + "' not found");
        }

        double distance = calculateHaversineDistance(
                city1.get().getGeographicInfo().getLatitude(),
                city1.get().getGeographicInfo().getLongitude(),
                city2.get().getGeographicInfo().getLatitude(),
                city2.get().getGeographicInfo().getLongitude()
        );

        // Estimation du temps de trajet (hypothèse : 80 km/h en moyenne)
        double travelTimeHours = distance / 80.0;

        return new CityDistanceDto(
                city1.get().getCityName(),
                city2.get().getCityName(),
                distance,
                travelTimeHours
        );
    }

    /**
     * Calcule la distance entre deux points géographiques en utilisant la formule de Haversine
     * @param lat1 Latitude du premier point
     * @param lon1 Longitude du premier point
     * @param lat2 Latitude du second point
     * @param lon2 Longitude du second point
     * @return Distance en kilomètres
     */
    private double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS_KM = 6378;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }
}