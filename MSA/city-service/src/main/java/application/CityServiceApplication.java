package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Microservice City-Service
 * Responsabilités :
 * - Gestion des villes
 * - Informations géographiques (coordonnées, distances)
 * - Base de données : PostgreSQL avec support JSON
 */

@SpringBootApplication(scanBasePackages = {"controller", "service", "repository", "model"})
@EnableJpaRepositories(basePackages = "repository")
@EntityScan(basePackages = "model")
public class CityServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CityServiceApplication.class, args);
    }
}