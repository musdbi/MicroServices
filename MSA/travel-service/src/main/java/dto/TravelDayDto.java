package dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class TravelDayDto {
    private Long id;

    @NotNull(message = "Date is required")
    private LocalDate date;

    private Integer dayNumber;

    @NotBlank(message = "Main city name is required")
    private String mainCityName;

    private Long mainCityId;
    private String accommodationCityName;
    private String accommodationId; // Référence vers poi-service

    // Constructeurs
    public TravelDayDto() {}

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getDayNumber() { return dayNumber; }
    public void setDayNumber(Integer dayNumber) { this.dayNumber = dayNumber; }

    public String getMainCityName() { return mainCityName; }
    public void setMainCityName(String mainCityName) { this.mainCityName = mainCityName; }

    public Long getMainCityId() { return mainCityId; }
    public void setMainCityId(Long mainCityId) { this.mainCityId = mainCityId; }

    public String getAccommodationCityName() { return accommodationCityName; }
    public void setAccommodationCityName(String accommodationCityName) { this.accommodationCityName = accommodationCityName; }

    public String getAccommodationId() { return accommodationId; }
    public void setAccommodationId(String accommodationId) { this.accommodationId = accommodationId; }
}

