package service;

import application.CityServiceApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.TestConfig;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Tests complets pour City-Service
 * Couvre toutes les fonctionnalités CRUD + Requêtes NoSQL
 */
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = CityServiceApplication.class)
@Import(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CityServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Long parisId;
    private static Long lyonId;
    private static Long versaillesId;

    @Test
    @Order(1)
    @DisplayName("1. Créer une ville - Paris")
    public void testCreateParis() throws Exception {
        String cityJson = """
            {
                "cityName": "Paris",
                "geographicInfo": {
                    "latitude": 48.8566,
                    "longitude": 2.3522,
                    "country": "France"
                }
            }
            """;

        MvcResult result = mockMvc.perform(post("/api/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cityJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cityName").value("Paris"))
                .andExpect(jsonPath("$.geographicInfo.country").value("France"))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        parisId = objectMapper.readTree(response).get("id").asLong();
        System.out.println("Paris créé avec ID: " + parisId);
    }

    @Test
    @Order(2)
    @DisplayName("2. Créer d'autres villes pour les tests")
    public void testCreateOtherCities() throws Exception {
        // Lyon
        String lyonJson = """
            {
                "cityName": "Lyon",
                "geographicInfo": {
                    "latitude": 45.7640,
                    "longitude": 4.8357,
                    "country": "France"
                }
            }
            """;

        MvcResult lyonResult = mockMvc.perform(post("/api/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(lyonJson))
                .andExpect(status().isCreated())
                .andReturn();

        lyonId = objectMapper.readTree(lyonResult.getResponse().getContentAsString()).get("id").asLong();

        // Versailles (proche de Paris)
        String versaillesJson = """
            {
                "cityName": "Versailles",
                "geographicInfo": {
                    "latitude": 48.8014,
                    "longitude": 2.1301,
                    "country": "France"
                }
            }
            """;

        MvcResult versaillesResult = mockMvc.perform(post("/api/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(versaillesJson))
                .andExpect(status().isCreated())
                .andReturn();

        versaillesId = objectMapper.readTree(versaillesResult.getResponse().getContentAsString()).get("id").asLong();

        System.out.println("Lyon créé avec ID: " + lyonId);
        System.out.println("Versailles créé avec ID: " + versaillesId);
    }

    @Test
    @Order(3)
    @DisplayName("3. Récupérer toutes les villes")
    public void testGetAllCities() throws Exception {
        mockMvc.perform(get("/api/cities"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[?(@.cityName=='Paris')]").exists())
                .andExpect(jsonPath("$[?(@.cityName=='Lyon')]").exists());

        System.out.println("Récupération de toutes les villes réussie");
    }

    @Test
    @Order(4)
    @DisplayName("4. Récupérer une ville par ID")
    public void testGetCityById() throws Exception {
        mockMvc.perform(get("/api/cities/" + parisId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(parisId))
                .andExpect(jsonPath("$.cityName").value("Paris"))
                .andExpect(jsonPath("$.geographicInfo.latitude").value(48.8566));

        System.out.println("Récupération de Paris par ID réussie");
    }

    @Test
    @Order(5)
    @DisplayName("5. Rechercher une ville par nom")
    public void testSearchCityByName() throws Exception {
        mockMvc.perform(get("/api/cities/search?name=Lyon"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cityName").value("Lyon"))
                .andExpect(jsonPath("$.geographicInfo.country").value("France"));

        System.out.println("Recherche de Lyon par nom réussie");
    }

    @Test
    @Order(6)
    @DisplayName("6. REQUÊTE NOSQL 1 - Villes dans un rayon de 50km autour de Paris")
    public void testNearbyCities() throws Exception {
        mockMvc.perform(get("/api/cities/nearby?city=Paris&radius=50"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[?(@.cityName=='Versailles')]").exists())
                .andExpect(jsonPath("$[?(@.cityName=='Paris')]").exists());

        System.out.println("REQUÊTE NOSQL 1: Villes proches de Paris trouvées");
    }

    @Test
    @Order(7)
    @DisplayName("7. REQUÊTE NOSQL 4 - Distance entre Paris et Lyon")
    public void testDistanceBetweenCities() throws Exception {
        mockMvc.perform(get("/api/cities/distance?city1=Paris&city2=Lyon"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cityName1").value("Paris"))
                .andExpect(jsonPath("$.cityName2").value("Lyon"))
                .andExpect(jsonPath("$.distanceKm").exists())
                .andExpect(jsonPath("$.travelTimeHours").exists())
                .andExpect(jsonPath("$.distanceKm").value(org.hamcrest.Matchers.greaterThan(300.0)));

        System.out.println("REQUÊTE NOSQL 4: Distance Paris-Lyon calculée");
    }

    @Test
    @Order(8)
    @DisplayName("8. Rechercher villes par pays")
    public void testGetCitiesByCountry() throws Exception {
        mockMvc.perform(get("/api/cities/country/France"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[?(@.cityName=='Paris')]").exists());

        System.out.println("Recherche par pays 'France' réussie");
    }

    @Test
    @Order(9)
    @DisplayName("9. Mettre à jour une ville")
    public void testUpdateCity() throws Exception {
        String updateJson = """
            {
                "cityName": "Paris Capitale",
                "geographicInfo": {
                    "latitude": 48.8566,
                    "longitude": 2.3522,
                    "country": "France"
                }
            }
            """;

        mockMvc.perform(put("/api/cities/" + parisId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cityName").value("Paris Capitale"));

        System.out.println("Mise à jour de Paris réussie");
    }

    @Test
    @Order(10)
    @DisplayName("10. Tests d'erreurs - Ville inexistante")
    public void testErrorCases() throws Exception {
        // Ville inexistante par ID
        mockMvc.perform(get("/api/cities/99999"))
                .andDo(print())
                .andExpect(status().isNotFound());

        // Ville inexistante par nom
        mockMvc.perform(get("/api/cities/search?name=VilleInexistante"))
                .andDo(print())
                .andExpect(status().isNotFound());

        // Distance avec ville inexistante
        mockMvc.perform(get("/api/cities/distance?city1=Paris&city2=VilleInexistante"))
                .andDo(print())
                .andExpect(status().isBadRequest());

        System.out.println("Gestion d'erreurs validée");
    }

    @Test
    @Order(11)
    @DisplayName("11. Test validation - Ville déjà existante")
    public void testDuplicateCity() throws Exception {
        String duplicateJson = """
            {
                "cityName": "Paris Capitale",
                "geographicInfo": {
                    "latitude": 48.8566,
                    "longitude": 2.3522,
                    "country": "France"
                }
            }
            """;

        mockMvc.perform(post("/api/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(duplicateJson))
                .andDo(print())
                .andExpect(status().isBadRequest());

        System.out.println("Validation doublons réussie");
    }

    @Test
    @Order(12)
    @DisplayName("12. Test endpoint santé")
    public void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/api/cities/health"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("City Service is running")));

        System.out.println("Endpoint santé fonctionnel");
    }

    @Test
    @Order(13)
    @DisplayName("13. Supprimer une ville - Nettoyage")
    public void testDeleteCity() throws Exception {
        // Créer une ville temporaire
        String tempCityJson = """
            {
                "cityName": "VilleTest",
                "geographicInfo": {
                    "latitude": 50.0,
                    "longitude": 3.0,
                    "country": "France"
                }
            }
            """;

        MvcResult createResult = mockMvc.perform(post("/api/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tempCityJson))
                .andExpect(status().isCreated())
                .andReturn();

        Long tempId = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        // La supprimer
        mockMvc.perform(delete("/api/cities/" + tempId))
                .andDo(print())
                .andExpect(status().isNoContent());

        // Vérifier qu'elle n'existe plus
        mockMvc.perform(get("/api/cities/" + tempId))
                .andExpect(status().isNotFound());

        System.out.println("Suppression de ville réussie");
    }

    @Test
    @Order(14)
    @DisplayName("14. Résumé des tests City-Service")
    public void testSummary() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("📋 RÉSUMÉ TESTS CITY-SERVICE");
        System.out.println("=".repeat(50));
        System.out.println("CRUD Complet: CREATE, READ, UPDATE, DELETE");
        System.out.println("Requête NoSQL 1: Villes dans un rayon donné");
        System.out.println("Requête NoSQL 4: Distance entre 2 villes");
        System.out.println("Validations: Doublons, erreurs, santé");
        System.out.println("Base PostgreSQL + JSON fonctionnelle");
        System.out.println("=".repeat(50));
    }
}