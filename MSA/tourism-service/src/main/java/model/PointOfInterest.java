package model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import model.GeographicInfo;

import jakarta.validation.constraints.NotBlank;

/**
 * Point d'Intérêt - Document MongoDB
 * Représente les lieux d'intérêt touristique (musées, monuments, parcs, etc.)
 */
@Document(collection = "points_of_interest")
public class PointOfInterest {

    @Id
    private String id;

    @NotBlank(message = "POI name is required")
    private String name;

    @Indexed
    private String cityName;

    @Indexed
    private Long cityId;

    private String type; // musée, monument, parc, etc.
    private String description;

    private GeographicInfo geographicInfo;

    public PointOfInterest() {}

    public PointOfInterest(String name, String cityName, Long cityId, String type, String description, GeographicInfo geographicInfo) {
        this.name = name;
        this.cityName = cityName;
        this.cityId = cityId;
        this.type = type;
        this.description = description;
        this.geographicInfo = geographicInfo;
    }

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

    public GeographicInfo getGeographicInfo() { return geographicInfo; }
    public void setGeographicInfo(GeographicInfo geographicInfo) { this.geographicInfo = geographicInfo; }

    @Override
    public String toString() {
        return "PointOfInterest{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", cityName='" + cityName + '\'' +
                ", type='" + type + '\'' +
                ", description=" + description +
                '}';
    }
}