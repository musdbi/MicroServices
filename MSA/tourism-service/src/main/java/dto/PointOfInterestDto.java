package dto;

import model.GeographicInfo;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO pour les Points d'Intérêt
 */

public class PointOfInterestDto {

    private String id;

    @NotBlank(message = "POI name is required")
    private String name;

    @NotBlank(message = "City name is required")
    private String cityName;

    private Long cityId;

    private String type;
    private String description;
    private GeographicInfo geographicInfo;

    public PointOfInterestDto() {}

    public PointOfInterestDto(String name, String cityName, Long cityId, String type,
                              String description, GeographicInfo geographicInfo) {
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
}