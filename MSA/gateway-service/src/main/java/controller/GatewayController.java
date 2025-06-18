// MSA/gateway-service/src/main/java/controller/GatewayController.java

package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

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
     * Méthode améliorée pour transmettre les requêtes avec le body
     */
    private ResponseEntity<String> forwardRequest(HttpServletRequest request, String body, String serviceUrl) {
        String path = request.getRequestURI().replace("/api", "");
        String queryString = request.getQueryString();
        String fullUrl = serviceUrl + "/api" + path + (queryString != null ? "?" + queryString : "");

        logger.info("Routing to service: {} {} with body: {}", request.getMethod(), fullUrl,
                body != null ? body.substring(0, Math.min(body.length(), 100)) + "..." : "null");

        try {
            HttpMethod method = HttpMethod.valueOf(request.getMethod());

            // Créer les headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Copier les headers de la requête originale (sauf Content-Length)
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                if (!headerName.equalsIgnoreCase("Content-Length") &&
                        !headerName.equalsIgnoreCase("Host")) {
                    headers.add(headerName, request.getHeader(headerName));
                }
            }

            HttpEntity<String> entity;
            if (method == HttpMethod.GET || method == HttpMethod.DELETE) {
                entity = new HttpEntity<>(headers);
            } else {
                // Pour POST, PUT, PATCH, on inclut le body
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

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("HTTP Error from service: {} - Body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode())
                    .body(e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("Error calling service: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Service unavailable: " + e.getMessage() + "\"}");
        }
    }

    /**
     * Méthode utilitaire pour lire le body de la requête
     */
    private String readRequestBody(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            logger.error("Error reading request body: {}", e.getMessage());
        }
        return stringBuilder.toString();
    }

    @RequestMapping(value = "/cities/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> routeToCityService(HttpServletRequest request) {
        String body = null;
        if (request.getMethod().equals("POST") || request.getMethod().equals("PUT")) {
            body = readRequestBody(request);
        }
        return forwardRequest(request, body, cityServiceUrl);
    }

    @RequestMapping(value = "/tourism/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> routeToTourismService(HttpServletRequest request) {
        String body = null;
        if (request.getMethod().equals("POST") || request.getMethod().equals("PUT")) {
            body = readRequestBody(request);
        }
        return forwardRequest(request, body, tourismServiceUrl);
    }

    @RequestMapping(value = "/travels/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> routeToTravelService(HttpServletRequest request) {
        String body = null;
        if (request.getMethod().equals("POST") || request.getMethod().equals("PUT") || request.getMethod().equals("PATCH")) {
            body = readRequestBody(request);
        }
        return forwardRequest(request, body, travelServiceUrl);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("{\"status\":\"Gateway Service is running!\",\"services\":{\"city\":\"" + cityServiceUrl + "\",\"tourism\":\"" + tourismServiceUrl + "\",\"travel\":\"" + travelServiceUrl + "\"}}");
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