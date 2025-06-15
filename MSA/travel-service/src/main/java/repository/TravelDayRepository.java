package repository;

import model.TravelDay;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelDayRepository extends Neo4jRepository<TravelDay, Long> {

    @Query("MATCH (t:Travel)-[:HAS_DAY]->(td:TravelDay) " +
            "WHERE t.id = $travelId " +
            "RETURN td ORDER BY td.dayNumber ASC")
    List<TravelDay> findByTravelIdOrderByDayNumber(@Param("travelId") Long travelId);

    @Query("MATCH (td:TravelDay), (cv:CityVisit) " +
            "WHERE td.id = $dayId AND cv.id = $visitId " +
            "CREATE (td)-[:VISITS]->(cv)")
    void createVisitsRelation(@Param("dayId") Long dayId, @Param("visitId") Long visitId);
}
