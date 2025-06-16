package model;

import org.springframework.data.neo4j.core.schema.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * Journée de voyage simplifiée - Nœud Neo4j
 * Contient directement les activités planifiées pour cette journée
 */
@Node("TravelDay")
public class TravelDay {

    @Id @GeneratedValue
    private Long id;

    @NotNull(message = "Date is required")
    private LocalDate date;

    private Integer dayNumber; // Jour 1, 2, 3...

    // Hébergement de la journée (null pour le dernier jour)
    private String accommodationCityName;
    private Long accommodationCityId; // Référence vers city-service
    private String accommodationId; // Référence vers tourism-service

    // Activités planifiées pour cette journée (IDs seulement)
    private List<String> plannedActivityIds;

    // Notes et budget de la journée
    private String dayDescription;
    private Double dailyBudget;

    public TravelDay() {}

    public TravelDay(LocalDate date, Integer dayNumber) {
        this.date = date;
        this.dayNumber = dayNumber;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getDayNumber() { return dayNumber; }
    public void setDayNumber(Integer dayNumber) { this.dayNumber = dayNumber; }

    public String getAccommodationCityName() { return accommodationCityName; }
    public void setAccommodationCityName(String accommodationCityName) { this.accommodationCityName = accommodationCityName; }

    public Long getAccommodationCityId() { return accommodationCityId; }
    public void setAccommodationCityId(Long accommodationCityId) { this.accommodationCityId = accommodationCityId; }

    public String getAccommodationId() { return accommodationId; }
    public void setAccommodationId(String accommodationId) { this.accommodationId = accommodationId; }

    public List<String> getPlannedActivityIds() { return plannedActivityIds; }
    public void setPlannedActivityIds(List<String> plannedActivityIds) { this.plannedActivityIds = plannedActivityIds; }

    public String getDayDescription() { return dayDescription; }
    public void setDayDescription(String dayDescription) { this.dayDescription = dayDescription; }

    public Double getDailyBudget() { return dailyBudget; }
    public void setDailyBudget(Double dailyBudget) { this.dailyBudget = dailyBudget; }

    @Override
    public String toString() {
        return "TravelDay{" +
                "id=" + id +
                ", date=" + date +
                ", dayNumber=" + dayNumber +
                ", accommodationCityName='" + accommodationCityName + '\'' +
                ", plannedActivityIds=" + plannedActivityIds +
                '}';
    }
}