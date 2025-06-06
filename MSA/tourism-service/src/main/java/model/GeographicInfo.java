package model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe pour les informations géographiques
 * Similaire à celle du city-service mais sans le pays
 */
public class GeographicInfo {

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("longitude")
    private Double longitude;

    public GeographicInfo() {}

    public GeographicInfo(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "GeographicInfo{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
