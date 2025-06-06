package model;

import org.springframework.data.neo4j.core.schema.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Visite de ville enrichie - Nœud Neo4j
 * Contient les références vers POI et activités du poi-service
 */
@Node("CityVisit")
public class CityVisit {

    @Id @GeneratedValue
    private Long id;

    @NotBlank(message = "City name is required")
    private String cityName;

    private Long cityId; // Référence vers city-service
    private LocalDate visitDate;
    private LocalTime arrivalTime;   // Heure d'arrivée dans la ville
    private LocalTime departureTime; // Heure de départ
    private String transportMode;    // train, car, bus, plane, walking

    // Références vers poi-service (IDs seulement, objets récupérés via REST)
    private List<String> plannedPOIIds;      // POI à visiter
    private List<String> plannedActivityIds; // Activités prévues

    // Informations de visite
    private String visitDescription;
    private Double visitBudget;
    private String visitStatus; // planned, in_progress, completed, cancelled

    // Constructeurs
    public CityVisit() {}

    public CityVisit(String cityName, Long cityId, LocalDate visitDate) {
        this.cityName = cityName;
        this.cityId = cityId;
        this.visitDate = visitDate;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    public Long getCityId() { return cityId; }
    public void setCityId(Long cityId) { this.cityId = cityId; }

    public LocalDate getVisitDate() { return visitDate; }
    public void setVisitDate(LocalDate visitDate) { this.visitDate = visitDate; }

    public LocalTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalTime arrivalTime) { this.arrivalTime = arrivalTime; }

    public LocalTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalTime departureTime) { this.departureTime = departureTime; }

    public String getTransportMode() { return transportMode; }
    public void setTransportMode(String transportMode) { this.transportMode = transportMode; }

    public List<String> getPlannedPOIIds() { return plannedPOIIds; }
    public void setPlannedPOIIds(List<String> plannedPOIIds) { this.plannedPOIIds = plannedPOIIds; }

    public List<String> getPlannedActivityIds() { return plannedActivityIds; }
    public void setPlannedActivityIds(List<String> plannedActivityIds) { this.plannedActivityIds = plannedActivityIds; }

    public String getVisitDescription() { return visitDescription; }
    public void setVisitDescription(String visitDescription) { this.visitDescription = visitDescription; }

    public Double getVisitBudget() { return visitBudget; }
    public void setVisitBudget(Double visitBudget) { this.visitBudget = visitBudget; }

    public String getVisitStatus() { return visitStatus; }
    public void setVisitStatus(String visitStatus) { this.visitStatus = visitStatus; }

    @Override
    public String toString() {
        return "CityVisit{" +
                "id=" + id +
                ", cityName='" + cityName + '\'' +
                ", visitDate=" + visitDate +
                ", arrivalTime=" + arrivalTime +
                ", departureTime=" + departureTime +
                ", transportMode='" + transportMode + '\'' +
                ", visitStatus='" + visitStatus + '\'' +
                '}';
    }
}