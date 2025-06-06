package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Gateway Controller - Routage vers tous les microservices
 * Point d'entrée unique sur le port 8080
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class GatewayController {

    private static final Logger logger = LoggerFactory.getLogger(GatewayController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${microservices.city-service.url:http://localhost:8081}")
    private String cityServiceUrl;

    @Value("${microservices.tourism-service.url:http://localhost:8082}")
    private String tourismServiceUrl;

    @Value("${microservices.travel-service.url:http://localhost:8083}")
    private String travelServiceUrl;

    /**
     * Route TOUTES les requêtes /api/cities/** vers city-service
     */
    @RequestMapping(value = "/cities/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> routeToCityService(
            HttpServletRequest request,
            @RequestBody(required = false) Object body) {

        String path = request.getRequestURI().replace("/api", "");
        String queryString = request.getQueryString();
        String fullUrl = cityServiceUrl + "/api" + path + (queryString != null ? "?" + queryString : "");

        logger.info("Routing to city-service: {} {}", request.getMethod(), fullUrl);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<Object> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    fullUrl,
                    HttpMethod.valueOf(request.getMethod()),
                    entity,
                    String.class
            );

            logger.info("City-service responded with status: {}", response.getStatusCode());
            return response;

        } catch (Exception e) {
            logger.error("Error calling city-service: {}", e.getMessage());
            return ResponseEntity.status(500).body("{\"error\":\"City service unavailable: " + e.getMessage() + "\"}");
        }
    }

    /**
     * Route TOUTES les requêtes /api/tourism/** vers tourism-service
     */
    @RequestMapping(value = "/tourism/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> routeToTourismService(
            HttpServletRequest request,
            @RequestBody(required = false) Object body) {

        String path = request.getRequestURI().replace("/api", "");
        String queryString = request.getQueryString();
        String fullUrl = tourismServiceUrl + "/api" + path + (queryString != null ? "?" + queryString : "");

        logger.info("Routing to tourism-service: {} {}", request.getMethod(), fullUrl);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<Object> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    fullUrl,
                    HttpMethod.valueOf(request.getMethod()),
                    entity,
                    String.class
            );

            logger.info("Tourism-service responded with status: {}", response.getStatusCode());
            return response;

        } catch (Exception e) {
            logger.error("Error calling tourism-service: {}", e.getMessage());
            return ResponseEntity.status(500).body("{\"error\":\"tourism service unavailable: " + e.getMessage() + "\"}");
        }
    }

    /**
     * Route TOUTES les requêtes /api/travels/** vers travel-service
     */
    @RequestMapping(value = "/travels/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> routeToTravelService(
            HttpServletRequest request,
            @RequestBody(required = false) Object body) {

        String path = request.getRequestURI().replace("/api", "");
        String queryString = request.getQueryString();
        String fullUrl = travelServiceUrl + "/api" + path + (queryString != null ? "?" + queryString : "");

        logger.info("Routing to travel-service: {} {}", request.getMethod(), fullUrl);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<Object> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    fullUrl,
                    HttpMethod.valueOf(request.getMethod()),
                    entity,
                    String.class
            );

            logger.info("Travel-service responded with status: {}", response.getStatusCode());
            return response;

        } catch (Exception e) {
            logger.error("Error calling travel-service: {}", e.getMessage());
            return ResponseEntity.status(500).body("{\"error\":\"Travel service unavailable: " + e.getMessage() + "\"}");
        }
    }

    /**
     * Endpoint de santé du gateway
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("{\"status\":\"Gateway Service is running! 🚪\",\"services\":{\"city\":\"" + cityServiceUrl + "\",\"tourism\":\"" + tourismServiceUrl + "\",\"travel\":\"" + travelServiceUrl + "\"}}");
    }

    /**
     * Information sur les routes disponibles
     */
    @GetMapping("/routes")
    public ResponseEntity<String> getRoutes() {
        return ResponseEntity.ok(String.format("""
            {
                "gateway": "http://localhost:8080",
                "routes": {
                    "cities": "/api/cities/**",
                    "tourism": "/api/tourism/**",
                    "travels": "/api/travels/**"
                },
                "services": {
                    "city-service": "%s",
                    "tourism-service": "%s",
                    "travel-service": "%s"
                },
                "endpoints": {
                    "health": "/api/health",
                    "routes": "/api/routes"
                }
            }
            """, cityServiceUrl, tourismServiceUrl, travelServiceUrl));
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Service running: " + this.getClass().getSimpleName());
    }
}