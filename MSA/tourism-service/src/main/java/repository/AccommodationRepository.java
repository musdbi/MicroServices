package repository;

import model.Accommodation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccommodationRepository extends MongoRepository<Accommodation, String> {

    // Recherche par ville (nom)
    List<Accommodation> findByCityNameIgnoreCase(String cityName);

    // Recherche par ville (ID) - important pour le projet NoSQL
    List<Accommodation> findByCityId(Long cityId);

    // Recherche par type
    List<Accommodation> findByTypeIgnoreCase(String type);

    // Recherche par ville et type
    List<Accommodation> findByCityNameIgnoreCaseAndTypeIgnoreCase(String cityName, String type);

    // Recherche par nom (autocomplétion)
    List<Accommodation> findByNameContainingIgnoreCase(String name);

    // Recherche par gamme de prix
    List<Accommodation> findByPricePerNightBetween(Double minPrice, Double maxPrice);

    // Recherche par ville et gamme de prix
    List<Accommodation> findByCityNameIgnoreCaseAndPricePerNightBetween(String cityName, Double minPrice, Double maxPrice);

    // Vérifier l'existence par nom et ville
    boolean existsByNameIgnoreCaseAndCityNameIgnoreCase(String name, String cityName);
}