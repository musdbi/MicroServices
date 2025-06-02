package model;

import org.springframework.data.neo4j.core.schema.*;

import java.time.LocalDate;

/**
 * Visite de ville - Nœud Neo4j simplifié
 */
@Node("CityVisit")
public class CityVisit {

    @Id @GeneratedValue
    private Long id;

    private String cityName; // Référence à la ville (du city-service)
    private LocalDate visitDate;
    private Integer dayNumber; // Jour du voyage

    // Constructeurs
    public CityVisit() {}

    public CityVisit(String cityName, LocalDate visitDate, Integer dayNumber) {
        this.cityName = cityName;
        this.visitDate = visitDate;
        this.dayNumber = dayNumber;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    public LocalDate getVisitDate() { return visitDate; }
    public void setVisitDate(LocalDate visitDate) { this.visitDate = visitDate; }

    public Integer getDayNumber() { return dayNumber; }
    public void setDayNumber(Integer dayNumber) { this.dayNumber = dayNumber; }

    @Override
    public String toString() {
        return "CityVisit{" +
                "id=" + id +
                ", cityName='" + cityName + '\'' +
                ", visitDate=" + visitDate +
                ", dayNumber=" + dayNumber +
                '}';
    }
}