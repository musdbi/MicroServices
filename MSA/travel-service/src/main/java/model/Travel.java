package model;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.schema.Relationship;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * Voyage enrichi - Nœud Neo4j principal
 * Orchestrateur qui référence les services externes
 */
@Node("Travel")
public class Travel {

    @Id @GeneratedValue
    private Long id;

    @NotBlank(message = "Travel name is required")
    private String travelName;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private String description;
    private String travelType; // business, adventure, cultural
    private Double estimatedBudget;
    private Integer travelers; // Nombre de voyageurs

    // Relations vers les journées de voyage
    @Relationship(type = "HAS_DAY", direction = Relationship.Direction.OUTGOING)
    private List<TravelDay> travelDays;

    // Constructeurs
    public Travel() {}

    public Travel(String travelName, LocalDate startDate, LocalDate endDate, String description) {
        this.travelName = travelName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTravelName() { return travelName; }
    public void setTravelName(String travelName) { this.travelName = travelName; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTravelType() { return travelType; }
    public void setTravelType(String travelType) { this.travelType = travelType; }

    public Double getEstimatedBudget() { return estimatedBudget; }
    public void setEstimatedBudget(Double estimatedBudget) { this.estimatedBudget = estimatedBudget; }

    public Integer getTravelers() { return travelers; }
    public void setTravelers(Integer travelers) { this.travelers = travelers; }

    public List<TravelDay> getTravelDays() { return travelDays; }
    public void setTravelDays(List<TravelDay> travelDays) { this.travelDays = travelDays; }

    @Override
    public String toString() {
        return "Travel{" +
                "id=" + id +
                ", travelName='" + travelName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", travelType='" + travelType + '\'' +
                ", travelers=" + travelers +
                '}';
    }
}