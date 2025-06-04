package repository;

import model.Travel;
import model.CityVisit;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository pour les Voyages Neo4j avec requêtes optimisées
 */
@Repository
public interface TravelRepository extends Neo4jRepository<Travel, Long> {

    /**
     * Trouve un voyage par son nom
     */
    Optional<Travel> findByTravelNameIgnoreCase(String travelName);

    /**
     * Trouve les voyages qui commencent dans une période donnée
     */
    List<Travel> findByStartDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Trouve un voyage avec toutes ses visites de villes triées par jour
     */
    @Query("MATCH (t:Travel)-[r:VISITS]->(c:CityVisit) " +
            "WHERE t.id = $travelId " +
            "RETURN t, collect(r), collect(c) " +
            "ORDER BY c.dayNumber")
    Optional<Travel> findTravelWithCityVisits(@Param("travelId") Long travelId);

    /**
     * Trouve les voyages qui visitent une ville donnée
     */
    @Query("MATCH (t:Travel)-[:VISITS]->(c:CityVisit) " +
            "WHERE c.cityName = $cityName " +
            "RETURN DISTINCT t")
    List<Travel> findTravelsByCityVisited(@Param("cityName") String cityName);

    /**
     * Trouve les villes possibles à visiter entre deux villes
     * Requête NoSQL améliorée avec gestion de l'ordre chronologique
     */
    @Query("MATCH (t:Travel)-[:VISITS]->(start:CityVisit), " +
            "(t)-[:VISITS]->(end:CityVisit), " +
            "(t)-[:VISITS]->(intermediate:CityVisit) " +
            "WHERE start.cityName = $startCity " +
            "AND end.cityName = $endCity " +
            "AND intermediate.cityName <> $startCity " +
            "AND intermediate.cityName <> $endCity " +
            "AND start.dayNumber < intermediate.dayNumber " +
            "AND intermediate.dayNumber < end.dayNumber " +
            "WITH DISTINCT intermediate.cityName as cityName, " +
            "     COUNT(t) as frequency " +
            "RETURN cityName " +
            "ORDER BY frequency DESC, cityName ASC")
    List<String> findIntermediateCities(@Param("startCity") String startCity, @Param("endCity") String endCity);

    /**
     * Trouve les voyages actifs (en cours ou futurs)
     */
    @Query("MATCH (t:Travel) " +
            "WHERE t.endDate >= date() " +
            "RETURN t " +
            "ORDER BY t.startDate")
    List<Travel> findActiveTravels();

    /**
     * Statistiques sur les villes les plus visitées
     */
    @Query("MATCH (t:Travel)-[:VISITS]->(c:CityVisit) " +
            "RETURN c.cityName as cityName, COUNT(c) as visitCount " +
            "ORDER BY visitCount DESC " +
            "LIMIT 10")
    List<CityVisitStat> getMostVisitedCities();

    /**
     * Interface pour les statistiques
     */
    interface CityVisitStat {
        String getCityName();
        Long getVisitCount();
    }
}