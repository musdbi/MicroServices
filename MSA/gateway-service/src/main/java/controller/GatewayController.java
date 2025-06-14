package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
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

    private ResponseEntity<String> forwardRequest(HttpServletRequest request, Object body, String serviceUrl) {
        String path = request.getRequestURI().replace("/api", "");
        String queryString = request.getQueryString();
        String fullUrl = serviceUrl + "/api" + path + (queryString != null ? "?" + queryString : "");

        logger.info("Routing to service: {} {}", request.getMethod(), fullUrl);

        try {
            HttpMethod method;
            try {
                method = HttpMethod.valueOf(request.getMethod());
            } catch (IllegalArgumentException e) {
                logger.error("Unsupported HTTP method: {}", request.getMethod());
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                        .body("{\"error\":\"Unsupported HTTP method\"}");
            }

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<?> entity;
            if (method == HttpMethod.GET || method == HttpMethod.DELETE) {
                entity = new HttpEntity<>(headers);
            } else {
                entity = new HttpEntity<>(body, headers);
            }

            ResponseEntity<String> response = restTemplate.exchange(
                    fullUrl,
                    method,
                    entity,
                    String.class
            );

            logger.info("Service responded with status: {}", response.getStatusCode());
            return response;

        } catch (Exception e) {
            logger.error("Error calling service: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Service unavailable: " + e.getMessage() + "\"}");
        }
    }

    @RequestMapping(value = "/cities/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> routeToCityService(HttpServletRequest request, @RequestBody(required = false) Object body) {
        return forwardRequest(request, body, cityServiceUrl);
    }

    @RequestMapping(value = "/tourism/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> routeToTourismService(HttpServletRequest request, @RequestBody(required = false) Object body) {
        return forwardRequest(request, body, tourismServiceUrl);
    }

    @RequestMapping(value = "/travels/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> routeToTravelService(HttpServletRequest request, @RequestBody(required = false) Object body) {
        return forwardRequest(request, body, travelServiceUrl);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("{\"status\":\"Gateway Service is running! \",\"services\":{\"city\":\"" + cityServiceUrl + "\",\"tourism\":\"" + tourismServiceUrl + "\",\"travel\":\"" + travelServiceUrl + "\"}}");
    }

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
