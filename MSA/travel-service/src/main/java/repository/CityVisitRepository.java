package repository;

import model.CityVisit;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository simple pour les visites de villes
 */
@Repository
public interface CityVisitRepository extends Neo4jRepository<CityVisit, Long> {

    /**
     * Trouve toutes les visites d'un voyage
     */
    @Query("MATCH (t:Travel)-[:VISITS]->(c:CityVisit) " +
            "WHERE t.id = $travelId " +
            "RETURN c ORDER BY c.dayNumber")
    List<CityVisit> findVisitsByTravelId(@Param("travelId") Long travelId);

    /**
     * Trouve les visites par ville
     */
    List<CityVisit> findByCityNameIgnoreCase(String cityName);

    /**
     * Trouve les visites d'une date donnée
     */
    List<CityVisit> findByVisitDate(LocalDate visitDate);
}