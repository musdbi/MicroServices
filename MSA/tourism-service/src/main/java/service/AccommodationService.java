package service;

import model.Accommodation;
import dto.AccommodationDto;
import repository.AccommodationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class AccommodationService {

    @Autowired
    private AccommodationRepository accommodationRepository;

    // Créer un hébergement
    public Accommodation createAccommodation(AccommodationDto accommodationDto) {
        // Vérifier si l'hébergement existe déjà
        if (accommodationRepository.existsByNameIgnoreCaseAndCityNameIgnoreCase(
                accommodationDto.getName(), accommodationDto.getCityName())) {
            throw new RuntimeException("Accommodation already exists in this city");
        }

        // Convertir DTO vers entité
        Accommodation accommodation = new Accommodation(
                accommodationDto.getName(),
                accommodationDto.getCityName(),
                accommodationDto.getCityId(),
                accommodationDto.getType(),
                accommodationDto.getDescription(),
                accommodationDto.getPricePerNight(),
                accommodationDto.getReviews() != null ? accommodationDto.getReviews() : new ArrayList<>(),
                accommodationDto.getGeographicInfo()
        );

        return accommodationRepository.save(accommodation);
    }

    // Récupérer tous les hébergements
    public List<Accommodation> getAllAccommodations() {
        return accommodationRepository.findAll();
    }

    // Récupérer un hébergement par ID
    public Optional<Accommodation> getAccommodationById(String id) {
        return accommodationRepository.findById(id);
    }

    // Récupérer les hébergements par ville (nom) - requête NoSQL importante
    public List<Accommodation> getAccommodationsByCity(String cityName) {
        return accommodationRepository.findByCityNameIgnoreCase(cityName);
    }

    // Récupérer les hébergements par ville (ID)
    public List<Accommodation> getAccommodationsByCityId(Long cityId) {
        return accommodationRepository.findByCityId(cityId);
    }

    // Récupérer les hébergements par type
    public List<Accommodation> getAccommodationsByType(String type) {
        return accommodationRepository.findByTypeIgnoreCase(type);
    }

    // Récupérer les hébergements par ville et type
    public List<Accommodation> getAccommodationsByCityAndType(String cityName, String type) {
        return accommodationRepository.findByCityNameIgnoreCaseAndTypeIgnoreCase(cityName, type);
    }

    // Rechercher par nom (autocomplétion)
    public List<Accommodation> searchAccommodationsByName(String name) {
        return accommodationRepository.findByNameContainingIgnoreCase(name);
    }

    // Rechercher par gamme de prix
    public List<Accommodation> getAccommodationsByPriceRange(Double minPrice, Double maxPrice) {
        return accommodationRepository.findByPricePerNightBetween(minPrice, maxPrice);
    }

    // Rechercher par ville et gamme de prix
    public List<Accommodation> getAccommodationsByCityAndPriceRange(String cityName, Double minPrice, Double maxPrice) {
        return accommodationRepository.findByCityNameIgnoreCaseAndPricePerNightBetween(cityName, minPrice, maxPrice);
    }

    // Hébergements avec au moins X avis
    public List<Accommodation> getAccommodationsWithMinimumReviews(int minReviews) {
        return accommodationRepository.findByReviewsSizeGreaterThanEqual(minReviews);
    }

    // Ajouter un avis
    public Accommodation addReview(String accommodationId, String review) {
        return accommodationRepository.findById(accommodationId).map(accommodation -> {
            if (accommodation.getReviews() == null) {
                accommodation.setReviews(new ArrayList<>());
            }
            accommodation.getReviews().add(review);
            return accommodationRepository.save(accommodation);
        }).orElseThrow(() -> new RuntimeException("Accommodation not found with id: " + accommodationId));
    }

    // Mettre à jour un hébergement
    public Accommodation updateAccommodation(String id, AccommodationDto accommodationDto) {
        return accommodationRepository.findById(id).map(existingAccommodation -> {
            existingAccommodation.setName(accommodationDto.getName());
            existingAccommodation.setCityName(accommodationDto.getCityName());
            existingAccommodation.setCityId(accommodationDto.getCityId());
            existingAccommodation.setType(accommodationDto.getType());
            existingAccommodation.setDescription(accommodationDto.getDescription());
            existingAccommodation.setPricePerNight(accommodationDto.getPricePerNight());
            existingAccommodation.setReviews(accommodationDto.getReviews());
            existingAccommodation.setGeographicInfo(accommodationDto.getGeographicInfo());
            return accommodationRepository.save(existingAccommodation);
        }).orElseThrow(() -> new RuntimeException("Accommodation not found with id: " + id));
    }

    // Supprimer un hébergement
    public void deleteAccommodation(String id) {
        if (!accommodationRepository.existsById(id)) {
            throw new RuntimeException("Accommodation not found with id: " + id);
        }
        accommodationRepository.deleteById(id);
    }

    // Convertir entité vers DTO
    public AccommodationDto convertToDto(Accommodation accommodation) {
        AccommodationDto dto = new AccommodationDto();
        dto.setId(accommodation.getId());
        dto.setName(accommodation.getName());
        dto.setCityName(accommodation.getCityName());
        dto.setCityId(accommodation.getCityId());
        dto.setType(accommodation.getType());
        dto.setDescription(accommodation.getDescription());
        dto.setPricePerNight(accommodation.getPricePerNight());
        dto.setReviews(accommodation.getReviews());
        dto.setGeographicInfo(accommodation.getGeographicInfo());
        return dto;
    }
}