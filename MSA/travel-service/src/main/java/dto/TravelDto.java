package dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class TravelDto {
    private Long id;

    @NotBlank(message = "Travel name is required")
    private String travelName;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private String description;

    public TravelDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTravelName() { return travelName; }
    public void setTravelName(String travelName) { this.travelName = travelName; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}