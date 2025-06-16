package repository;

import model.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends MongoRepository<Activity, String> {

    // Recherche par ville (nom)
    List<Activity> findByCityNameIgnoreCase(String cityName);

    // Recherche par ville (ID)
    List<Activity> findByCityId(Long cityId);

    // Recherche par type
    List<Activity> findByTypeIgnoreCase(String type);

    // Recherche par ville et type
    List<Activity> findByCityNameIgnoreCaseAndTypeIgnoreCase(String cityName, String type);

    // Recherche par nom (autocomplétion)
    List<Activity> findByNameContainingIgnoreCase(String name);

    // Recherche par gamme de prix
    List<Activity> findByPriceBetween(Double minPrice, Double maxPrice);

    // Recherche par durée (utile pour la planification)
    List<Activity> findByDurationMinutesBetween(Integer minDuration, Integer maxDuration);

    // REQUÊTE NOSQL IMPORTANTE : Activités associées à un POI donné
    List<Activity> findByPointOfInterestIdsContaining(String pointOfInterestId);

    // REQUÊTE NOSQL IMPORTANTE : Activités par mois disponibles
    @Query("{ 'availableMonths': { $in: ?0 } }")
    List<Activity> findByAvailableMonthsIn(List<Integer> months);

    // Activités avec plusieurs POI (circuits, tours)
    @Query("{ 'pointOfInterestIds': { $size: { $gte: ?0 } } }")
    List<Activity> findActivitiesWithMinimumPOIs(int minPOIs);

    // Activités gratuites
    List<Activity> findByPriceEquals(Double price); // Pour price = 0.0

    // Activités par ville avec gamme de prix
    List<Activity> findByCityNameIgnoreCaseAndPriceBetween(String cityName, Double minPrice, Double maxPrice);

    // Vérifier l'existence par nom et ville
    boolean existsByNameIgnoreCaseAndCityNameIgnoreCase(String name, String cityName);
}