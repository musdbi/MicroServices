package fr.dauphine.miageif.msa.travelservice.model;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.schema.Relationship;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * Voyage simplifié - Nœud Neo4j
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

    // Relations vers les villes visitées
    @Relationship(type = "VISITS", direction = Relationship.Direction.OUTGOING)
    private List<CityVisit> cityVisits;

    // Constructeurs
    public Travel() {}

    public Travel(String travelName, LocalDate startDate, LocalDate endDate) {
        this.travelName = travelName;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public List<CityVisit> getCityVisits() { return cityVisits; }
    public void setCityVisits(List<CityVisit> cityVisits) { this.cityVisits = cityVisits; }

    @Override
    public String toString() {
        return "Travel{" +
                "id=" + id +
                ", travelName='" + travelName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}

/**
 * Visite de ville - Nœud Neo4j simplifié
 */
@Node("CityVisit")
public class CityVisit {

    @Id @GeneratedValue
    private Long id;

    private String cityName; // Référence à la ville (du city-service)
    private LocalDate visitDate;
    private Integer dayNumber; // Jour du voyage

    // Constructeurs
    public CityVisit() {}

    public CityVisit(String cityName, LocalDate visitDate, Integer dayNumber) {
        this.cityName = cityName;
        this.visitDate = visitDate;
        this.dayNumber = dayNumber;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    public LocalDate getVisitDate() { return visitDate; }
    public void setVisitDate(LocalDate visitDate) { this.visitDate = visitDate; }

    public Integer getDayNumber() { return dayNumber; }
    public void setDayNumber(Integer dayNumber) { this.dayNumber = dayNumber; }

    @Override
    public String toString() {
        return "CityVisit{" +
                "id=" + id +
                ", cityName='" + cityName + '\'' +
                ", visitDate=" + visitDate +
                ", dayNumber=" + dayNumber +
                '}';
    }
}