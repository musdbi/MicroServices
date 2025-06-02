package repository;

import model.PointOfInterest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository simple pour les Points d'Intérêt
 * Répond aux requêtes NoSQL de base
 */
@Repository
public interface PointOfInterestRepository extends MongoRepository<PointOfInterest, String> {

    /**
     * Trouve tous les POI d'une ville donnée
     * Répond à la requête NoSQL : "Quels sont les hébergements d'une ville ?"
     * (étendu à tous les POI)
     */
    List<PointOfInterest> findByCityNameIgnoreCase(String cityName);

    /**
     * Trouve les POI par type
     * Exemples : "hotel", "museum", "restaurant", "activity"
     */
    List<PointOfInterest> findByType(String type);

    /**
     * Trouve les hébergements d'une ville (type = "hotel")
     * Répond exactement à la requête NoSQL
     */
    List<PointOfInterest> findByCityNameIgnoreCaseAndType(String cityName, String type);

    /**
     * Recherche par nom (pour autocomplétion)
     */
    List<PointOfInterest> findByNameContainingIgnoreCase(String nameFragment);

    /**
     * Trouve un POI par son nom exact
     */
    Optional<PointOfInterest> findByNameIgnoreCase(String name);
}