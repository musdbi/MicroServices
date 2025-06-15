package dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class TravelDayDto {
    private Long id;

    @NotNull(message = "Date is required")
    private LocalDate date;

    private Integer dayNumber;

    // Hébergement (optionnel)
    private String accommodationCityName;
    private Long accommodationCityId;
    private String accommodationId;

    // Activités planifiées pour cette journée
    private List<String> plannedActivityIds;

    private String dayDescription;
    private Double dailyBudget;

    // Constructeurs
    public TravelDayDto() {}

    // Getters et Setters
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
}