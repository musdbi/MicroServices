package repository;

import model.CityVisit;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityVisitRepository extends Neo4jRepository<CityVisit, Long> {
    // Juste les méthodes de base héritées
}