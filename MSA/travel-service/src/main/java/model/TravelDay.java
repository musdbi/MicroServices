package model;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.schema.Relationship;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * Journée de voyage enrichie - Nœud Neo4j
 * Peut contenir plusieurs villes visitées dans la même journée
 */
@Node("TravelDay")
public class TravelDay {

    @Id @GeneratedValue
    private Long id;

    @NotNull(message = "Date is required")
    private LocalDate date;

    private Integer dayNumber; // Jour 1, 2, 3...

    // Ville principale de la journée
    private String mainCityName;
    private Long mainCityId; // Référence vers city-service

    // Hébergement (peut être dans une ville différente des visites)
    private String accommodationCityName;
    private Long accommodationCityId;
    private String accommodationId; // Référence vers poi-service

    // Notes et budget de la journée
    private String dayDescription;
    private Double dailyBudget;

    // Relations vers les visites de villes
    @Relationship(type = "VISITS", direction = Relationship.Direction.OUTGOING)
    private List<CityVisit> cityVisits;

    // Constructeurs
    public TravelDay() {}

    public TravelDay(LocalDate date, Integer dayNumber, String mainCityName, Long mainCityId) {
        this.date = date;
        this.dayNumber = dayNumber;
        this.mainCityName = mainCityName;
        this.mainCityId = mainCityId;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getDayNumber() { return dayNumber; }
    public void setDayNumber(Integer dayNumber) { this.dayNumber = dayNumber; }

    public String getMainCityName() { return mainCityName; }
    public void setMainCityName(String mainCityName) { this.mainCityName = mainCityName; }

    public Long getMainCityId() { return mainCityId; }
    public void setMainCityId(Long mainCityId) { this.mainCityId = mainCityId; }

    public String getAccommodationCityName() { return accommodationCityName; }
    public void setAccommodationCityName(String accommodationCityName) { this.accommodationCityName = accommodationCityName; }

    public Long getAccommodationCityId() { return accommodationCityId; }
    public void setAccommodationCityId(Long accommodationCityId) { this.accommodationCityId = accommodationCityId; }

    public String getAccommodationId() { return accommodationId; }
    public void setAccommodationId(String accommodationId) { this.accommodationId = accommodationId; }

    public String getDayDescription() { return dayDescription; }
    public void setDayDescription(String dayDescription) { this.dayDescription = dayDescription; }

    public Double getDailyBudget() { return dailyBudget; }
    public void setDailyBudget(Double dailyBudget) { this.dailyBudget = dailyBudget; }

    public List<CityVisit> getCityVisits() { return cityVisits; }
    public void setCityVisits(List<CityVisit> cityVisits) { this.cityVisits = cityVisits; }

    @Override
    public String toString() {
        return "TravelDay{" +
                "id=" + id +
                ", date=" + date +
                ", dayNumber=" + dayNumber +
                ", mainCityName='" + mainCityName + '\'' +
                ", accommodationCityName='" + accommodationCityName + '\'' +
                '}';
    }
}