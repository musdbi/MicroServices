package fr.dauphine.miageif.msa.travelservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
public class TravelServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelServiceApplication.class, args);
    }
}