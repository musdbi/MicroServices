package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Microservice POI-Service
 * Responsabilités :
 * - Gestion des Points d'Intérêt (CRUD)
 * - Gestion des Activités associées aux POI
 * - Gestion des Hébergements
 * - Base de données : MongoDB (documents flexibles)
 */
@SpringBootApplication
@ComponentScan(basePackages = {"controller", "service", "repository", "model", "config"})
@EnableMongoRepositories(basePackages = "repository")
public class TourismServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TourismServiceApplication.class, args);
    }
}