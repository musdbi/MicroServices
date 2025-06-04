package model;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.schema.Relationship;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;

/**
 * Visite de ville - Nœud Neo4j avec relation bidirectionnelle
 */
@Node("CityVisit")
public class CityVisit {

    @Id @GeneratedValue
    private Long id;

    @NotBlank(message = "City name is required")
    private String cityName; // Référence à la ville (du city-service)

    @NotNull(message = "Visit date is required")
    private LocalDate visitDate;

    @NotNull(message = "Day number is required")
    @Min(value = 1, message = "Day number must be positive")
    private Integer dayNumber; // Jour du voyage

    // Relation inverse vers le voyage
    @Relationship(type = "VISITS", direction = Relationship.Direction.INCOMING)
    private Travel travel;

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

    public Travel getTravel() { return travel; }
    public void setTravel(Travel travel) { this.travel = travel; }

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