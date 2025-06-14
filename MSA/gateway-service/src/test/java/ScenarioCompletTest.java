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
public class ScenarioCompletTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    public void testScenarioComplet() throws Exception {
        // 1. Vérifier que tous les services sont up
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk());

        // 2. Créer une ville via le gateway
        String cityJson = """
            {
                "cityName": "Nice",
                "geographicInfo": {
                    "latitude": 43.7102,
                    "longitude": 7.2620,
                    "country": "France"
                }
            }
            """;

        mockMvc.perform(post("/api/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cityJson))
                .andExpect(status().isCreated());

        // 3. Vérifier les requêtes NoSQL principales
        mockMvc.perform(get("/api/cities/nearby?city=Nice&radius=50"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/tourism/activities/april-to-june"))
                .andExpect(status().isOk());

        System.out.println("✅ Scénario complet validé !");
    }
}