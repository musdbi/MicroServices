package repository;

import model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité City
 */

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    /**
     * Recherche une ville par son nom
     */
    Optional<City> findByCityNameIgnoreCase(String cityName);

    /**
     * Recherche toutes les villes d'un pays donné
     */
    @Query(value = "SELECT * FROM cities c WHERE c.geographic_info->>'country' = :country",
            nativeQuery = true)
    List<City> findByCountry(@Param("country") String country);

    /**
     * Vérifie si une ville existe par son nom
     */
    boolean existsByCityNameIgnoreCase(String cityName);

    /**
     * Recherche les villes dans un rayon donné (utilise les coordonnées GPS)
     * Cette requête calcule la distance en utilisant la formule de Haversine
     *
     * @param latitude Latitude du point de référence
     * @param longitude Longitude du point de référence
     * @param radiusKm Rayon de recherche en kilomètres
     * @return Liste des villes dans le rayon spécifié
     */
    @Query(value = """
        SELECT * FROM cities c 
        WHERE (6371 * acos(
            cos(radians(:latitude)) * 
            cos(radians(CAST(c.geographic_info->>'latitude' AS DOUBLE PRECISION))) * 
            cos(radians(CAST(c.geographic_info->>'longitude' AS DOUBLE PRECISION)) - radians(:longitude)) + 
            sin(radians(:latitude)) * 
            sin(radians(CAST(c.geographic_info->>'latitude' AS DOUBLE PRECISION)))
        )) <= :radiusKm
        """, nativeQuery = true)
    List<City> findCitiesWithinRadius(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("radiusKm") Double radiusKm
    );
}