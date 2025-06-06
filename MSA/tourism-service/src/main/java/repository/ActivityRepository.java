package repository;

import model.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    // REQUÊTE NOSQL IMPORTANTE : Activités entre avril et juin (mois 4, 5, 6)
    @Query("{ 'availableMonths': { $in: ?0 } }")
    List<Activity> findByAvailableMonthsIn(List<Integer> months);

    // Activités disponibles sur une période spécifique
    @Query("{ $or: [ " +
            "{ 'startDate': { $lte: ?1 }, 'endDate': { $gte: ?0 } }, " +
            "{ 'availableMonths': { $in: ?2 } } " +
            "] }")
    List<Activity> findActivitiesAvailableBetween(LocalDate startDate, LocalDate endDate, List<Integer> months);

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