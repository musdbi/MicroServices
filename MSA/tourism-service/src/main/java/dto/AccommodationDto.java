package dto;

import model.GeographicInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

import java.util.List;

/**
 * DTO pour les Hébergements
 * Utilisé pour les échanges API (request/response)
 */

public class AccommodationDto {

    private String id;

    @NotBlank(message = "Accommodation name is required")
    private String name;

    @NotBlank(message = "City name is required")
    private String cityName;

    private Long cityId;

    private String type; // hotel, auberge, appartement, etc.
    private String description;

    @Min(value = 0, message = "Price must be positive")
    private Double pricePerNight;

    private List<String> reviews;
    private GeographicInfo geographicInfo;

    // Constructeurs
    public AccommodationDto() {}

    public AccommodationDto(String name, String cityName, Long cityId, String type, String description,
                            Double pricePerNight, List<String> reviews, GeographicInfo geographicInfo) {
        this.name = name;
        this.cityName = cityName;
        this.cityId = cityId;
        this.type = type;
        this.description = description;
        this.pricePerNight = pricePerNight;
        this.reviews = reviews;
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

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(Double pricePerNight) { this.pricePerNight = pricePerNight; }

    public List<String> getReviews() { return reviews; }
    public void setReviews(List<String> reviews) { this.reviews = reviews; }

    public GeographicInfo getGeographicInfo() { return geographicInfo; }
    public void setGeographicInfo(GeographicInfo geographicInfo) { this.geographicInfo = geographicInfo; }
}