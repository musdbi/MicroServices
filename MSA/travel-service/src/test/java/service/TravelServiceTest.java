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
public class TravelServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Long travelId;
    private static Long dayId;

    @Test
    @Order(1)
    public void testCreateTravel() throws Exception {
        String travelJson = """
            {
                "travelName": "Voyage Test Paris-Lyon",
                "startDate": "2025-07-01",
                "endDate": "2025-07-05",
                "description": "Voyage de test"
            }
            """;

        MvcResult result = mockMvc.perform(post("/api/travels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(travelJson))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        travelId = objectMapper.readTree(response).get("id").asLong();
    }

    @Test
    @Order(2)
    public void testGetAllTravels() throws Exception {
        mockMvc.perform(get("/api/travels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(3)
    public void testAddTravelDay() throws Exception {
        String dayJson = """
            {
                "date": "2025-07-01",
                "dayNumber": 1,
                "mainCityName": "Paris",
                "mainCityId": 1,
                "accommodationId": "hotel123",
                "accommodationCityName": "Paris"
            }
            """;

        MvcResult result = mockMvc.perform(post("/api/travels/" + travelId + "/days")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dayJson))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        dayId = objectMapper.readTree(response).get("id").asLong();
    }

    @Test
    @Order(4)
    public void testAddLastDayWithoutAccommodation() throws Exception {
        // Test règle métier: pas d'hébergement le dernier jour
        String lastDayJson = """
            {
                "date": "2025-07-05",
                "dayNumber": 5,
                "mainCityName": "Lyon",
                "mainCityId": 2,
                "accommodationId": "hotel456"
            }
            """;

        mockMvc.perform(post("/api/travels/" + travelId + "/days")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(lastDayJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accommodationId").doesNotExist());
    }

    @Test
    @Order(5)
    public void testAddCityVisit() throws Exception {
        String visitJson = """
            {
                "cityName": "Paris",
                "cityId": 1,
                "visitDate": "2025-07-01",
                "plannedPOIIds": ["poi1", "poi2"],
                "plannedActivityIds": ["act1"]
            }
            """;

        mockMvc.perform(post("/api/travels/" + travelId + "/days/" + dayId + "/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(visitJson))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(6)
    public void testFindIntermediateCities() throws Exception {
        // Test requête NoSQL 6: Villes intermédiaires
        mockMvc.perform(get("/api/travels/intermediate-cities?start=Paris&end=Lyon"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(7)
    public void testUpdateTravel() throws Exception {
        String updateJson = """
            {
                "travelName": "Voyage Modifié",
                "startDate": "2025-07-01",
                "endDate": "2025-07-06",
                "description": "Description modifiée"
            }
            """;

        mockMvc.perform(put("/api/travels/" + travelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    public void testDeleteTravel() throws Exception {
        // Créer un voyage temporaire
        String tempTravel = """
            {
                "travelName": "Voyage à supprimer",
                "startDate": "2025-08-01",
                "endDate": "2025-08-03",
                "description": "Test suppression"
            }
            """;

        MvcResult result = mockMvc.perform(post("/api/travels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tempTravel))
                .andExpect(status().isCreated())
                .andReturn();

        Long tempId = objectMapper.readTree(result.getResponse().getContentAsString())
                .get("id").asLong();

        // Le supprimer
        mockMvc.perform(delete("/api/travels/" + tempId))
                .andExpect(status().isNoContent());
    }
}