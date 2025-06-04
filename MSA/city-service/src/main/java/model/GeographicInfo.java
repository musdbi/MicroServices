package model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe pour les informations géographiques
 */

public class GeographicInfo {

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("longitude")
    private Double longitude;

    @JsonProperty("country")
    private String country;

    public GeographicInfo() {}

    public GeographicInfo(Double latitude, Double longitude, String country) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "GeographicInfo{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", country='" + country + '\'' +
                '}';
    }
}