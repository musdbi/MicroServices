package repository;

import model.PointOfInterest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointOfInterestRepository extends MongoRepository<PointOfInterest, String> {

    // Recherche par ville (nom)
    List<PointOfInterest> findByCityNameIgnoreCase(String cityName);

    // Recherche par ville (ID)
    List<PointOfInterest> findByCityId(Long cityId);

    // Recherche par type
    List<PointOfInterest> findByTypeIgnoreCase(String type);

    // Recherche par ville et type
    List<PointOfInterest> findByCityNameIgnoreCaseAndTypeIgnoreCase(String cityName, String type);

    // Recherche par nom (utile pour l'autocomplétion)
    List<PointOfInterest> findByNameContainingIgnoreCase(String name);

    // Vérifier l'existence par nom et ville
    boolean existsByNameIgnoreCaseAndCityNameIgnoreCase(String name, String cityName);
}