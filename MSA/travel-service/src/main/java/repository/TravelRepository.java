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
            "WHERE ID(t) = $travelId " +
            "OPTIONAL MATCH (t)-[:HAS_DAY]->(td:TravelDay) " +
            "RETURN t, collect(td)")
    Optional<Travel> findTravelWithAllRelations(@Param("travelId") Long travelId);

    /**
     * Crée la relation HAS_DAY entre Travel et TravelDay
     */
    @Query("MATCH (t:Travel), (td:TravelDay) " +
            "WHERE ID(t) = $travelId AND ID(td) = $dayId " +
            "CREATE (t)-[:HAS_DAY]->(td)")
    void createHasDayRelation(@Param("travelId") Long travelId, @Param("dayId") Long dayId);

    /**
     * Voyages qui ont un hébergement dans une ville donnée
     */
    @Query("MATCH (t:Travel)-[:HAS_DAY]->(td:TravelDay) " +
            "WHERE td.accommodationCityName = $cityName " +
            "RETURN DISTINCT t")
    List<Travel> findTravelsByCityVisited(@Param("cityName") String cityName);

    /**
     * REQUÊTE NOSQL : Trouve les villes intermédiaires entre deux villes
     * Basée sur les hébergements !!!
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

    /**
     * SUPPRESSION EN CASCADE - Supprime un voyage et tous ses TravelDay associés
     */
    @Query("MATCH (t:Travel) " +
            "WHERE ID(t) = $travelId " +
            "OPTIONAL MATCH (t)-[:HAS_DAY]->(td:TravelDay) " +
            "DETACH DELETE t, td")
    void deleteTravelWithCascade(@Param("travelId") Long travelId);

    /**
     * Compte le nombre de TravelDay orphelins (sans Travel parent)
     */
    @Query("MATCH (td:TravelDay) " +
            "WHERE NOT EXISTS { MATCH (:Travel)-[:HAS_DAY]->(td) } " +
            "RETURN count(td)")
    Long countOrphanedTravelDays();

    /**
     * Supprime tous les TravelDay orphelins
     */
    @Query("MATCH (td:TravelDay) " +
            "WHERE NOT EXISTS { MATCH (:Travel)-[:HAS_DAY]->(td) } " +
            "DETACH DELETE td")
    void deleteOrphanedTravelDays();
}