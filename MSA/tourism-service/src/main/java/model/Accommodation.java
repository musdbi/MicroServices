package model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

import java.util.List;

/**
 * Hébergement - Document MongoDB
 * Représente les hôtels, auberges, gîtes, etc.
 */
@Document(collection = "accommodations")
public class Accommodation {

    @Id
    private String id;

    @NotBlank(message = "Accommodation name is required")
    private String name;

    @Indexed
    private String cityName;

    @Indexed
    private Long cityId;

    private String type; // hotel, auberge, appartement, etc.
    private String description;

    @Min(value = 0, message = "Price must be positive")
    private Double pricePerNight;

    private List<String> reviews;

    private GeographicInfo geographicInfo;

    // Constructeurs
    public Accommodation() {}

    public Accommodation(String name, String cityName, Long cityId, String type, String description,
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

    @Override
    public String toString() {
        return "Accommodation{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", cityName='" + cityName + '\'' +
                ", type='" + type + '\'' +
                ", pricePerNight=" + pricePerNight +
                ", reviews=" + reviews +
                ", geographicInfo=" + geographicInfo +
                '}';
    }
}

