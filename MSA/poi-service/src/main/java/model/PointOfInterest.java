package model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.NotBlank;

/**
 * Point d'Intérêt simplifié - Document MongoDB
 * Combine POI, activités et hébergements en un seul modèle simple
 */
@Document(collection = "points_of_interest")
public class PointOfInterest {

    @Id
    private String id;

    @NotBlank(message = "POI name is required")
    private String name;

    @Indexed
    private String cityName;

    private String type; // museum, park, hotel, restaurant, activity
    private String description;
    private Double price; // Prix si applicable

    // Constructeurs
    public PointOfInterest() {}

    public PointOfInterest(String name, String cityName, String type, String description, Double price) {
        this.name = name;
        this.cityName = cityName;
        this.type = type;
        this.description = description;
        this.price = price;
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    @Override
    public String toString() {
        return "PointOfInterest{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", cityName='" + cityName + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                '}';
    }
}