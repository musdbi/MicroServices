package dto;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class CityVisitDto {
    private Long id;

    @NotBlank(message = "City name is required")
    private String cityName;

    private Long cityId;
    private LocalDate visitDate;

    // Références simples vers poi-service (IDs seulement)
    private List<String> plannedPOIIds;
    private List<String> plannedActivityIds;

    // Constructeurs
    public CityVisitDto() {}

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    public Long getCityId() { return cityId; }
    public void setCityId(Long cityId) { this.cityId = cityId; }

    public LocalDate getVisitDate() { return visitDate; }
    public void setVisitDate(LocalDate visitDate) { this.visitDate = visitDate; }

    public List<String> getPlannedPOIIds() { return plannedPOIIds; }
    public void setPlannedPOIIds(List<String> plannedPOIIds) { this.plannedPOIIds = plannedPOIIds; }

    public List<String> getPlannedActivityIds() { return plannedActivityIds; }
    public void setPlannedActivityIds(List<String> plannedActivityIds) { this.plannedActivityIds = plannedActivityIds; }
}
