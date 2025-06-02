package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Stockée en PostgreSQL avec informations géographiques en JSON
 */

@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "City name is required")
    @Column(name = "city_name", nullable = false)
    private String cityName;

    /**
     * Informations géographiques stockées en JSON PostgreSQL
     * Exemple: {"latitude": 48.8566, "longitude": 2.3522, "country": "France"}
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "geographic_info", columnDefinition = "jsonb")
    private GeographicInfo geographicInfo;

    public City() {}

    public City(String cityName, GeographicInfo geographicInfo) {
        this.cityName = cityName;
        this.geographicInfo = geographicInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public GeographicInfo getGeographicInfo() {
        return geographicInfo;
    }

    public void setGeographicInfo(GeographicInfo geographicInfo) {
        this.geographicInfo = geographicInfo;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", cityName='" + cityName + '\'' +
                ", geographicInfo=" + geographicInfo +
                '}';
    }
}