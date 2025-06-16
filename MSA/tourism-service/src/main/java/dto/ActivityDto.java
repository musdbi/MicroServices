package dto;

import model.GeographicInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

import java.util.List;

/**
 * DTO pour les Activités
 * Utilisé pour les échanges API (request/response)
 */
public class ActivityDto {

    private String id;

    @NotBlank(message = "Activity name is required")
    private String name;

    @NotBlank(message = "City name is required")
    private String cityName;

    private Long cityId;

    private List<String> pointOfInterestIds; // POI associés

    private String type; // visit, excursion, workshop, tour, etc.
    private String description;

    @Min(value = 0, message = "Price must be positive")
    private Double price;

    @Min(value = 1, message = "Duration must be positive")
    private Integer durationMinutes;

    private List<Integer> availableMonths; // 1-12 pour janvier-décembre

    private String departureLocation; // Lieu de départ
    private GeographicInfo geographicInfo; // Coordonnées du point de départ

    // Constructeurs
    public ActivityDto() {}

    public ActivityDto(String name, String cityName, Long cityId, List<String> pointOfInterestIds,
                       String type, String description, Double price, Integer durationMinutes,
                       List<Integer> availableMonths, String departureLocation, GeographicInfo geographicInfo) {
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

    public String getDepartureLocation() { return departureLocation; }
    public void setDepartureLocation(String departureLocation) { this.departureLocation = departureLocation; }

    public GeographicInfo getGeographicInfo() { return geographicInfo; }
    public void setGeographicInfo(GeographicInfo geographicInfo) { this.geographicInfo = geographicInfo; }
}
