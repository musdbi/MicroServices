package service;

import model.Travel;
import model.TravelDay;
import model.CityVisit;
import repository.TravelRepository;
import repository.TravelDayRepository;
import repository.CityVisitRepository;
import dto.TravelDto;
import dto.TravelDayDto;
import dto.CityVisitDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service simplifié de gestion des voyages
 */
@Service
public class TravelService {

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private TravelDayRepository travelDayRepository;

    @Autowired
    private CityVisitRepository cityVisitRepository;

    // ===============================
    // CRUD VOYAGES
    // ===============================

    public List<Travel> getAllTravels() {
        return travelRepository.findAll();
    }

    public Optional<Travel> getTravelById(Long id) {
        return travelRepository.findTravelWithAllRelations(id);
    }

    public Travel createTravel(TravelDto travelDto) {
        if (travelDto.getStartDate().isAfter(travelDto.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        Travel travel = new Travel(
                travelDto.getTravelName(),
                travelDto.getStartDate(),
                travelDto.getEndDate(),
                travelDto.getDescription()
        );

        return travelRepository.save(travel);
    }

    public Travel updateTravel(Long id, TravelDto travelDto) {
        Travel existingTravel = travelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Travel with ID " + id + " not found"));

        if (travelDto.getStartDate().isAfter(travelDto.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        existingTravel.setTravelName(travelDto.getTravelName());
        existingTravel.setStartDate(travelDto.getStartDate());
        existingTravel.setEndDate(travelDto.getEndDate());
        existingTravel.setDescription(travelDto.getDescription());

        return travelRepository.save(existingTravel);
    }

    public void deleteTravel(Long id) {
        if (!travelRepository.existsById(id)) {
            throw new IllegalArgumentException("Travel with ID " + id + " not found");
        }
        travelRepository.deleteById(id);
    }

    // ===============================
    // GESTION DES JOURNÉES
    // ===============================

    public List<TravelDay> getTravelDays(Long travelId) {
        if (!travelRepository.existsById(travelId)) {
            throw new IllegalArgumentException("Travel with ID " + travelId + " not found");
        }
        return travelDayRepository.findByTravelIdOrderByDayNumber(travelId);
    }

    public TravelDay addTravelDay(Long travelId, TravelDayDto dayDto) {
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new IllegalArgumentException("Travel with ID " + travelId + " not found"));

        // Auto-calcul du numéro de jour si pas spécifié
        if (dayDto.getDayNumber() == null) {
            List<TravelDay> existingDays = travelDayRepository.findByTravelIdOrderByDayNumber(travelId);
            int nextDayNumber = existingDays.size() + 1;
            dayDto.setDayNumber(nextDayNumber);
        }

        TravelDay travelDay = new TravelDay(
                dayDto.getDate(),
                dayDto.getDayNumber(),
                dayDto.getMainCityName(),
                dayDto.getMainCityId()
        );

        travelDay.setAccommodationCityName(dayDto.getAccommodationCityName());
        travelDay.setAccommodationId(dayDto.getAccommodationId());

        return travelDayRepository.save(travelDay);
    }

    public void deleteTravelDay(Long travelId, Long dayId) {
        if (!travelDayRepository.existsById(dayId)) {
            throw new IllegalArgumentException("Travel day with ID " + dayId + " not found");
        }
        travelDayRepository.deleteById(dayId);
    }

    // ===============================
    // GESTION DES VISITES
    // ===============================

    public CityVisit addCityVisit(Long travelId, Long dayId, CityVisitDto visitDto) {
        TravelDay travelDay = travelDayRepository.findById(dayId)
                .orElseThrow(() -> new IllegalArgumentException("Travel day with ID " + dayId + " not found"));

        CityVisit cityVisit = new CityVisit(
                visitDto.getCityName(),
                visitDto.getCityId(),
                visitDto.getVisitDate()
        );

        cityVisit.setPlannedPOIIds(visitDto.getPlannedPOIIds());
        cityVisit.setPlannedActivityIds(visitDto.getPlannedActivityIds());

        return cityVisitRepository.save(cityVisit);
    }

    public void deleteCityVisit(Long visitId) {
        if (!cityVisitRepository.existsById(visitId)) {
            throw new IllegalArgumentException("City visit with ID " + visitId + " not found");
        }
        cityVisitRepository.deleteById(visitId);
    }

    // ===============================
    // REQUÊTES NOSQL
    // ===============================

    public List<String> findIntermediateCities(String startCity, String endCity) {
        return travelRepository.findIntermediateCities(startCity, endCity);
    }

    public List<Travel> getTravelsByCityVisited(String cityName) {
        return travelRepository.findTravelsByCityVisited(cityName);
    }

    // ===============================
    // CONVERSIONS DTO SIMPLES
    // ===============================

    public TravelDto convertToDto(Travel travel) {
        TravelDto dto = new TravelDto();
        dto.setId(travel.getId());
        dto.setTravelName(travel.getTravelName());
        dto.setStartDate(travel.getStartDate());
        dto.setEndDate(travel.getEndDate());
        dto.setDescription(travel.getDescription());
        return dto;
    }

    public TravelDayDto convertTravelDayToDto(TravelDay travelDay) {
        TravelDayDto dto = new TravelDayDto();
        dto.setId(travelDay.getId());
        dto.setDate(travelDay.getDate());
        dto.setDayNumber(travelDay.getDayNumber());
        dto.setMainCityName(travelDay.getMainCityName());
        dto.setMainCityId(travelDay.getMainCityId());
        dto.setAccommodationCityName(travelDay.getAccommodationCityName());
        dto.setAccommodationId(travelDay.getAccommodationId());
        return dto;
    }

    public CityVisitDto convertCityVisitToDto(CityVisit cityVisit) {
        CityVisitDto dto = new CityVisitDto();
        dto.setId(cityVisit.getId());
        dto.setCityName(cityVisit.getCityName());
        dto.setCityId(cityVisit.getCityId());
        dto.setVisitDate(cityVisit.getVisitDate());
        dto.setPlannedPOIIds(cityVisit.getPlannedPOIIds());
        dto.setPlannedActivityIds(cityVisit.getPlannedActivityIds());
        return dto;
    }
}