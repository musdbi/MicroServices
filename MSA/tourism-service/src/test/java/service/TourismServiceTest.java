package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TourismServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String tourEiffelId;
    private static String activityId;

    // ========== TESTS POI ==========

    @Test
    @Order(1)
    public void testCreatePOI() throws Exception {
        String poiJson = """
            {
                "name": "Tour Eiffel",
                "cityName": "Paris",
                "cityId": 1,
                "type": "monument",
                "description": "Monument emblématique de Paris",
                "geographicInfo": {
                    "latitude": 48.8584,
                    "longitude": 2.2945
                }
            }
            """;

        MvcResult result = mockMvc.perform(post("/api/tourism/poi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(poiJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Tour Eiffel"))
                .andReturn();

        // Sauvegarder l'ID pour les tests suivants
        String response = result.getResponse().getContentAsString();
        tourEiffelId = objectMapper.readTree(response).get("id").asText();
    }

    @Test
    @Order(2)
    public void testGetAllPOI() throws Exception {
        mockMvc.perform(get("/api/tourism/poi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(3)
    public void testGetPOIByCity() throws Exception {
        mockMvc.perform(get("/api/tourism/poi/city/Paris"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cityName").value("Paris"));
    }

    // ========== TESTS ACTIVITES ==========

    @Test
    @Order(4)
    public void testCreateActivity() throws Exception {
        String activityJson = String.format("""
            {
                "name": "Visite guidée Tour Eiffel",
                "cityName": "Paris",
                "cityId": 1,
                "pointOfInterestIds": ["%s"],
                "type": "visite",
                "description": "Visite complète avec guide",
                "price": 25.90,
                "durationMinutes": 120,
                "availableMonths": [4, 5, 6],
                "departureLocation": "Pied de la Tour"
            }
            """, tourEiffelId);

        MvcResult result = mockMvc.perform(post("/api/tourism/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(activityJson))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        activityId = objectMapper.readTree(response).get("id").asText();
    }

    @Test
    @Order(5)
    public void testGetActivitiesByPOI() throws Exception {
        // Test requête NoSQL 2: Activités d'un POI
        mockMvc.perform(get("/api/tourism/activities/poi/" + tourEiffelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Visite guidée Tour Eiffel"));
    }

    @Test
    @Order(6)
    public void testGetActivitiesAprilToJune() throws Exception {
        // Test requête NoSQL 5: Activités entre avril et juin
        mockMvc.perform(get("/api/tourism/activities/april-to-june"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // ========== TESTS HEBERGEMENTS ==========

    @Test
    @Order(7)
    public void testCreateAccommodation() throws Exception {
        String accommodationJson = """
            {
                "name": "Hôtel de la Tour",
                "cityName": "Paris",
                "cityId": 1,
                "type": "hotel",
                "description": "Hôtel 3 étoiles près de la Tour Eiffel",
                "pricePerNight": 120.00,
                "reviews": ["Très bien", "Bon rapport qualité-prix"],
                "geographicInfo": {
                    "latitude": 48.8600,
                    "longitude": 2.2900
                }
            }
            """;

        mockMvc.perform(post("/api/tourism/accommodations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accommodationJson))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(8)
    public void testGetAccommodationsByCity() throws Exception {
        // Test requête NoSQL 3: Hébergements d'une ville
        mockMvc.perform(get("/api/tourism/accommodations/city/Paris"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cityName").value("Paris"));
    }

    @Test
    @Order(9)
    public void testUpdatePOI() throws Exception {
        String updateJson = """
            {
                "name": "Tour Eiffel - Updated",
                "cityName": "Paris",
                "cityId": 1,
                "type": "monument",
                "description": "Description mise à jour"
            }
            """;

        mockMvc.perform(put("/api/tourism/poi/" + tourEiffelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk());
    }

    @Test
    @Order(10)
    public void testDeleteActivity() throws Exception {
        mockMvc.perform(delete("/api/tourism/activities/" + activityId))
                .andExpect(status().isNoContent());
    }
}