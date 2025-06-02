package fr.dauphine.miageif.msa.poiservice.dto;

/**
 * DTO simple pour les Points d'Intérêt
 * Évite d'exposer l'ID technique MongoDB
 */
public class PointOfInterestDto {

    private String name;
    private String cityName;
    private String type;
    private String description;
    private String formattedPrice; // Prix formaté au lieu du Double brut

    // Constructeurs
    public PointOfInterestDto() {}

    public PointOfInterestDto(String name, String cityName, String type, String description, String formattedPrice) {
        this.name = name;
        this.cityName = cityName;
        this.type = type;
        this.description = description;
        this.formattedPrice = formattedPrice;
    }

    // Getters et Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFormattedPrice() { return formattedPrice; }
    public void setFormattedPrice(String formattedPrice) { this.formattedPrice = formattedPrice; }

    /**
     * Méthode utilitaire pour convertir une entité en DTO
     */
    public static PointOfInterestDto fromEntity(fr.dauphine.miageif.msa.poiservice.model.PointOfInterest poi) {
        String formattedPrice = poi.getPrice() != null ?
            String.format("%.2f €", poi.getPrice()) : "Gratuit";

        return new PointOfInterestDto(
            poi.getName(),
            poi.getCityName(),
            poi.getType(),
            poi.getDescription(),
            formattedPrice
        );
    }
}