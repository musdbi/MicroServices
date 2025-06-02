package fr.dauphine.miageif.msa.cityservice.dto;

/**
 * DTO pour représenter la distance et le temps de trajet entre deux villes
 * Répond aux requêtes du projet NoSQL sur les distances
 */
public class CityDistanceDto {

    private String cityName1;
    private String cityName2;
    private double distanceKm;
    private double travelTimeHours;

    // Constructeurs
    public CityDistanceDto() {}

    public CityDistanceDto(String cityName1, String cityName2, double distanceKm, double travelTimeHours) {
        this.cityName1 = cityName1;
        this.cityName2 = cityName2;
        this.distanceKm = distanceKm;
        this.travelTimeHours = travelTimeHours;
    }

    // Getters et Setters
    public String getCityName1() {
        return cityName1;
    }

    public void setCityName1(String cityName1) {
        this.cityName1 = cityName1;
    }

    public String getCityName2() {
        return cityName2;
    }

    public void setCityName2(String cityName2) {
        this.cityName2 = cityName2;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public double getTravelTimeHours() {
        return travelTimeHours;
    }

    public void setTravelTimeHours(double travelTimeHours) {
        this.travelTimeHours = travelTimeHours;
    }

    // Méthodes utilitaires
    public String getTravelTimeFormatted() {
        int hours = (int) travelTimeHours;
        int minutes = (int) ((travelTimeHours - hours) * 60);
        return String.format("%dh %02dmin", hours, minutes);
    }

    public double getDistanceRounded() {
        return Math.round(distanceKm * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return "CityDistanceDto{" +
                "cityName1='" + cityName1 + '\'' +
                ", cityName2='" + cityName2 + '\'' +
                ", distanceKm=" + getDistanceRounded() +
                ", travelTime=" + getTravelTimeFormatted() +
                '}';
    }
}