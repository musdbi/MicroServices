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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CityServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    public void testCreateCity() throws Exception {
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

        mockMvc.perform(post("/api/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cityJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cityName").value("Paris"));
    }

    @Test
    @Order(2)
    public void testCreateMoreCities() throws Exception {
        // Créer Versailles (proche de Paris)
        String versailles = """
            {
                "cityName": "Versailles",
                "geographicInfo": {
                    "latitude": 48.8014,
                    "longitude": 2.1301,
                    "country": "France"
                }
            }
            """;
        mockMvc.perform(post("/api/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(versailles))
                .andExpect(status().isCreated());

        // Créer Lyon
        String lyon = """
            {
                "cityName": "Lyon",
                "geographicInfo": {
                    "latitude": 45.7640,
                    "longitude": 4.8357,
                    "country": "France"
                }
            }
            """;
        mockMvc.perform(post("/api/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(lyon))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(3)
    public void testGetAllCities() throws Exception {
        mockMvc.perform(get("/api/cities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(4)
    public void testGetCityByName() throws Exception {
        mockMvc.perform(get("/api/cities/search?name=Paris"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cityName").value("Paris"));
    }

    @Test
    @Order(5)
    public void testGetNearbyCities() throws Exception {
        // Test requête NoSQL 1: Villes à moins de 10km
        mockMvc.perform(get("/api/cities/nearby?city=Paris&radius=50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(6)
    public void testCalculateDistance() throws Exception {
        // Test requête NoSQL 4: Distance entre 2 villes
        mockMvc.perform(get("/api/cities/distance?city1=Paris&city2=Lyon"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.distanceKm").exists())
                .andExpect(jsonPath("$.travelTimeHours").exists());
    }

    @Test
    @Order(7)
    public void testUpdateCity() throws Exception {
        String updateJson = """
            {
                "cityName": "Paris Updated",
                "geographicInfo": {
                    "latitude": 48.8566,
                    "longitude": 2.3522,
                    "country": "France"
                }
            }
            """;

        mockMvc.perform(put("/api/cities/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    public void testDeleteCity() throws Exception {
        // Créer une ville temporaire
        String tempCity = """
            {
                "cityName": "VilleTemp",
                "geographicInfo": {
                    "latitude": 50.0,
                    "longitude": 3.0,
                    "country": "France"
                }
            }
            """;
        mockMvc.perform(post("/api/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tempCity))
                .andExpect(status().isCreated());

        // La supprimer
        mockMvc.perform(delete("/api/cities/4"))
                .andExpect(status().isNoContent());
    }
}