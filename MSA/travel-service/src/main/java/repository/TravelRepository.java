package repository;

import model.Travel;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TravelRepository extends Neo4jRepository<Travel, Long> {

    /**
     * Trouve un voyage avec toutes ses relations
     */
    @Query("MATCH (t:Travel) " +
            "WHERE t.id = $travelId " +
            "OPTIONAL MATCH (t)-[:HAS_DAY]->(td:TravelDay) " +
            "RETURN t, collect(td)")
    Optional<Travel> findTravelWithAllRelations(@Param("travelId") Long travelId);

    /**
     * Crée la relation HAS_DAY entre Travel et TravelDay
     */
    @Query("MATCH (t:Travel), (td:TravelDay) " +
            "WHERE t.id = $travelId AND td.id = $dayId " +
            "CREATE (t)-[:HAS_DAY]->(td)")
    void createHasDayRelation(@Param("travelId") Long travelId, @Param("dayId") Long dayId);

    /**
     * REQUÊTE NOSQL : Villes intermédiaires entre deux villes
     * Modifiée pour utiliser les activités au lieu des CityVisit
     */
    @Query("MATCH (t:Travel)-[:HAS_DAY]->(td:TravelDay) " +
            "WHERE EXISTS(td.plannedActivityIds) " +
            "WITH t, td " +
            "ORDER BY td.dayNumber " +
            "WITH t, collect(td) as days " +
            "UNWIND range(0, size(days)-1) as idx " +
            "WITH t, days[idx] as day " +
            "WHERE " +
            "  (idx = 0 AND day.accommodationCityName = $startCity) OR " +
            "  (idx = size(days)-1 AND " +
            "   ((day.accommodationCityName = $endCity) OR " +
            "    (day.accommodationCityName IS NULL AND idx > 0 AND days[idx-1].accommodationCityName = $endCity))) " +
            "RETURN DISTINCT t")
    List<Travel> findTravelsWithRoute(@Param("startCity") String startCity, @Param("endCity") String endCity);

    /**
     * Trouve les villes intermédiaires (simplifié car on n'a plus les villes visitées directement)
     * Cette requête devra être implémentée côté service en récupérant les activités
     */
    @Query("MATCH (t:Travel)-[:HAS_DAY]->(td:TravelDay) " +
            "WHERE td.accommodationCityName IS NOT NULL " +
            "RETURN DISTINCT td.accommodationCityName as cityName " +
            "ORDER BY cityName")
    List<String> findAllAccommodationCities();

    /**
     * Voyages qui ont un hébergement dans une ville donnée
     */
    @Query("MATCH (t:Travel)-[:HAS_DAY]->(td:TravelDay) " +
            "WHERE td.accommodationCityName = $cityName " +
            "RETURN DISTINCT t")
    List<Travel> findTravelsByCityVisited(@Param("cityName") String cityName);

    /**
     * REQUÊTE NOSQL : Trouve les villes intermédiaires entre deux villes
     * Basée sur les hébergements (car on n'a plus accès direct aux villes des activités)
     */
    @Query("MATCH (t:Travel)-[:HAS_DAY]->(td1:TravelDay), " +
            "(t)-[:HAS_DAY]->(td2:TravelDay), " +
            "(t)-[:HAS_DAY]->(tdMiddle:TravelDay) " +
            "WHERE td1.accommodationCityName = $startCity " +
            "AND td2.accommodationCityName = $endCity " +
            "AND tdMiddle.accommodationCityName IS NOT NULL " +
            "AND tdMiddle.accommodationCityName <> $startCity " +
            "AND tdMiddle.accommodationCityName <> $endCity " +
            "AND td1.dayNumber < tdMiddle.dayNumber " +
            "AND tdMiddle.dayNumber < td2.dayNumber " +
            "RETURN DISTINCT tdMiddle.accommodationCityName as cityName")
    List<String> findIntermediateCities(@Param("startCity") String startCity, @Param("endCity") String endCity);
}