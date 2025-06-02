package fr.dauphine.miageif.msa.cityservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Microservice City-Service
 * Responsabilités :
 * - Gestion des villes (CRUD)
 * - Informations géographiques (coordonnées, distances)
 * - Base de données : PostgreSQL avec support JSON
 */
@SpringBootApplication
public class CityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CityServiceApplication.class, args);
    }
}