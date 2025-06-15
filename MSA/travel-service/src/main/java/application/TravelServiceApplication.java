package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

/**
 * Microservice Travel-Service
 * Responsabilités :
 * - Planification de voyages (itinéraires, étapes)
 * - Gestion des relations entre entités (graphes)
 * - Recommandations basées sur Neo4j
 * - Calculs d'itinéraires optimaux
 * - Communication avec city-service et poi-service
 * - Base de données : Neo4j (graphes de relations)
 */
@SpringBootApplication
@ComponentScan(basePackages = {"controller", "service", "repository", "model", "dto", "config"})
@EnableNeo4jRepositories(basePackages = "repository")
public class TravelServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelServiceApplication.class, args);
    }
}