package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Microservice POI-Service
 * Responsabilités :
 * - Gestion des Points d'Intérêt (CRUD)
 * - Gestion des Activités associées aux POI
 * - Gestion des Hébergements
 * - Base de données : MongoDB (documents flexibles)
 */
@SpringBootApplication
public class PoiServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PoiServiceApplication.class, args);
    }
}