package dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO simple pour les Voyages
 * Évite d'exposer l'ID technique Neo4j
 */
public class TravelDto {

    private String travelName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private int durationDays;
    private List<CityVisitDto> cityVisits;

    // Constructeurs
    public TravelDto() {}

    public TravelDto(String travelName, LocalDate startDate, LocalDate endDate, String description, List<CityVisitDto> cityVisits) {
        this.travelName = travelName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.cityVisits = cityVisits;
        this.durationDays = calculateDuration(startDate, endDate);
    }

    // Getters et Setters
    public String getTravelName() { return travelName; }
    public void setTravelName(String travelName) { this.travelName = travelName; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getDurationDays() { return durationDays; }
    public void setDurationDays(int durationDays) { this.durationDays = durationDays; }

    public List<CityVisitDto> getCityVisits() { return cityVisits; }
    public void setCityVisits(List<CityVisitDto> cityVisits) { this.cityVisits = cityVisits; }

    // Méthodes utilitaires
    private int calculateDuration(LocalDate start, LocalDate end) {
        if (start != null && end != null) {
            return (int) (end.toEpochDay() - start.toEpochDay()) + 1;
        }
        return 0;
    }

    /**
     * Conversion depuis l'entité
     */
    public static TravelDto fromEntity(model.Travel travel) {
        List<CityVisitDto> visitDtos = travel.getCityVisits() != null ?
            travel.getCityVisits().stream()
                .map(CityVisitDto::fromEntity)
                .collect(Collectors.toList()) : List.of();

        return new TravelDto(
            travel.getTravelName(),
            travel.getStartDate(),
            travel.getEndDate(),
            travel.getDescription(),
            visitDtos
        );
    }
}

/**
 * DTO simple pour les visites de villes
 */
class CityVisitDto {

    private String cityName;
    private LocalDate visitDate;
    private Integer dayNumber;

    // Constructeurs
    public CityVisitDto() {}

    public CityVisitDto(String cityName, LocalDate visitDate, Integer dayNumber) {
        this.cityName = cityName;
        this.visitDate = visitDate;
        this.dayNumber = dayNumber;
    }

    // Getters et Setters
    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    public LocalDate getVisitDate() { return visitDate; }
    public void setVisitDate(LocalDate visitDate) { this.visitDate = visitDate; }

    public Integer getDayNumber() { return dayNumber; }
    public void setDayNumber(Integer dayNumber) { this.dayNumber = dayNumber; }

    /**
     * Conversion depuis l'entité
     */
    public static CityVisitDto fromEntity(model.CityVisit visit) {
        return new CityVisitDto(
            visit.getCityName(),
            visit.getVisitDate(),
            visit.getDayNumber()
        );
    }
}