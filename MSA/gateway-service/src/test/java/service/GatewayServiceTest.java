package service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GatewayServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGatewayHealth() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists());
    }

    @Test
    public void testGatewayRoutes() throws Exception {
        mockMvc.perform(get("/api/routes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.routes").exists());
    }

    @Test
    public void testRoutingToCityService() throws Exception {
        // Test que le gateway route bien vers city-service
        mockMvc.perform(get("/api/cities/health"))
                .andExpect(status().isOk());
    }

    @Test
    public void testRoutingToTourismService() throws Exception {
        // Test que le gateway route bien vers tourism-service
        mockMvc.perform(get("/api/tourism/poi/test"))
                .andExpect(status().isOk());
    }

    @Test
    public void testRoutingToTravelService() throws Exception {
        // Test que le gateway route bien vers travel-service
        mockMvc.perform(get("/api/travels/health"))
                .andExpect(status().isOk());
    }
}