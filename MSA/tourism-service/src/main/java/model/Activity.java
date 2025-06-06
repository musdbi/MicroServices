package model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import java.util.List;

/**
 * Activité - Document MongoDB
 * Représente les activités liées aux POI (visites, excursions)
 */
@Document(collection = "activities")
public class Activity {

    @Id
    private String id;

    @NotBlank(message = "Activity name is required")
    private String name;

    @Indexed
    private String cityName;

    @Indexed
    private Long cityId;

    // Références aux POI associés (optionnel car une activité peut être indépendante)
    @Indexed
    private List<String> pointOfInterestIds;

    private String type;
    private String description;

    @Min(value = 0, message = "Price must be positive")
    private Double price;

    @Min(value = 1, message = "Duration must be positive")
    private Integer durationMinutes;

    // Saisonnalité (pour répondre à la requête "activités entre avril et juin")
    private List<Integer> availableMonths;

    // Dates spécifiques de disponibilité (optionnel)
    private LocalDate startDate;
    private LocalDate endDate;

    // Lieu de départ de l'activité (optionnel)
    private String departureLocation;

    // Coordonnées géographiques du point de départ
    private GeographicInfo geographicInfo;

    // Constructeurs
    public Activity() {}

    public Activity(String name, String cityName, Long cityId, List<String> pointOfInterestIds, String type,
                    String description, Double price, Integer durationMinutes, List<Integer> availableMonths,
                    String departureLocation, GeographicInfo geographicInfo) {
        this.name = name;
        this.cityName = cityName;
        this.cityId = cityId;
        this.pointOfInterestIds = pointOfInterestIds;
        this.type = type;
        this.description = description;
        this.price = price;
        this.durationMinutes = durationMinutes;
        this.availableMonths = availableMonths;
        this.departureLocation = departureLocation;
        this.geographicInfo = geographicInfo;
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    public Long getCityId() { return cityId; }
    public void setCityId(Long cityId) { this.cityId = cityId; }

    public List<String> getPointOfInterestIds() { return pointOfInterestIds; }
    public void setPointOfInterestIds(List<String> pointOfInterestIds) { this.pointOfInterestIds = pointOfInterestIds; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public List<Integer> getAvailableMonths() { return availableMonths; }
    public void setAvailableMonths(List<Integer> availableMonths) { this.availableMonths = availableMonths; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getDepartureLocation() { return departureLocation; }
    public void setDepartureLocation(String departureLocation) { this.departureLocation = departureLocation; }

    public GeographicInfo getGeographicInfo() { return geographicInfo; }
    public void setGeographicInfo(GeographicInfo geographicInfo) { this.geographicInfo = geographicInfo; }

    @Override
    public String toString() {
        return "Activity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", cityName='" + cityName + '\'' +
                ", pointOfInterestIds=" + pointOfInterestIds +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", durationMinutes=" + durationMinutes +
                ", availableMonths=" + availableMonths +
                ", geographicInfo=" + geographicInfo +
                '}';
    }
}