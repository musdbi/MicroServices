package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

/**
 * Gateway Service - Point d'entrée unique
 * Responsabilités :
 * - Routage vers les microservices appropriés
 * - Gestion CORS centralisée
 * - Monitoring des requêtes
 * - Port : 8080 (port standard pour gateway)
 */
@SpringBootApplication
@ComponentScan(basePackages = {"controller", "application"})
public class GatewayServiceApplication {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }
}