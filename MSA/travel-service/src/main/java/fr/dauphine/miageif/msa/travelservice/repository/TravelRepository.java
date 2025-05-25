package fr.dauphine.miageif.msa.travelservice.repository;

import fr.dauphine.miageif.msa.travelservice.model.Travel;
import fr.dauphine.miageif.msa.travelservice.model.CityVisit;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository simple pour les Voyages Neo4j
 * Focus sur les requêtes essentielles
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
     * Trouve un voyage avec toutes ses visites de villes
     * Requête Cypher simple pour récupérer le graphe
     */
    @Query("MATCH (t:Travel)-[r:VISITS]->(c:CityVisit) " +
            "WHERE t.id = $travelId " +
            "RETURN t, collect(r), collect(c)")
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
     * Répond à la requête NoSQL : "Étant données une ville de départ et une ville d'arrivée,
     * quels sont les différentes villes possibles à visiter entre les 2 ?"
     * Version simplifiée basée sur les voyages existants
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
            "RETURN DISTINCT intermediate.cityName as cityName " +
            "ORDER BY cityName")
    List<String> findIntermediateCities(@Param("startCity") String startCity, @Param("endCity") String endCity);
}

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