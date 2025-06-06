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
            "OPTIONAL MATCH (td)-[:VISITS]->(cv:CityVisit) " +
            "RETURN t, collect(td), collect(cv)")
    Optional<Travel> findTravelWithAllRelations(@Param("travelId") Long travelId);

    /**
     * REQUÊTE NOSQL PRINCIPALE : Villes intermédiaires entre deux villes
     */
    @Query("MATCH (t:Travel)-[:HAS_DAY]->(start_day:TravelDay), " +
            "(t)-[:HAS_DAY]->(end_day:TravelDay), " +
            "(t)-[:HAS_DAY]->(intermediate_day:TravelDay) " +
            "WHERE start_day.mainCityName = $startCity " +
            "AND end_day.mainCityName = $endCity " +
            "AND intermediate_day.mainCityName <> $startCity " +
            "AND intermediate_day.mainCityName <> $endCity " +
            "AND start_day.dayNumber < intermediate_day.dayNumber " +
            "AND intermediate_day.dayNumber < end_day.dayNumber " +
            "RETURN DISTINCT intermediate_day.mainCityName as cityName")
    List<String> findIntermediateCities(@Param("startCity") String startCity, @Param("endCity") String endCity);

    /**
     * Voyages qui visitent une ville donnée
     */
    @Query("MATCH (t:Travel)-[:HAS_DAY]->(td:TravelDay) " +
            "WHERE td.mainCityName = $cityName " +
            "RETURN DISTINCT t")
    List<Travel> findTravelsByCityVisited(@Param("cityName") String cityName);
}
