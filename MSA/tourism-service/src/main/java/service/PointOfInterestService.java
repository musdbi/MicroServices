package service;

import model.PointOfInterest;
import dto.PointOfInterestDto;
import repository.PointOfInterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;
import java.util.Map;

@Service
public class PointOfInterestService {

    @Autowired
    private PointOfInterestRepository poiRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${services.city-service.url:http://localhost:8081}")
    private String cityServiceUrl;

    private void validateCityExists(String cityName) {
        if (cityName == null || cityName.isEmpty()) {
            return;
        }

        try {
            String url = cityServiceUrl + "/api/cities/search?name=" + cityName;
            Map<String, Object> city = restTemplate.getForObject(url, Map.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("Ville '" + cityName + "' introuvable dans le système");
        } catch (Exception e) {
            throw new RuntimeException("Impossible de vérifier l'existence de la ville: " + e.getMessage());
        }
    }

    // Modifie la méthode createPointOfInterest
    public PointOfInterest createPointOfInterest(PointOfInterestDto poiDto) {
        // Vérifier si le POI existe déjà
        if (poiRepository.existsByNameIgnoreCaseAndCityNameIgnoreCase(poiDto.getName(), poiDto.getCityName())) {
            throw new RuntimeException("Point of Interest already exists in this city");
        }

        // VALIDATION : Vérifier que la ville existe
        validateCityExists(poiDto.getCityName());

        // Convertir DTO vers entité
        PointOfInterest poi = new PointOfInterest(
                poiDto.getName(),
                poiDto.getCityName(),
                poiDto.getCityId(),
                poiDto.getType(),
                poiDto.getDescription(),
                poiDto.getGeographicInfo()
        );

        return poiRepository.save(poi);
    }

    // Récupérer tous les POI
    public List<PointOfInterest> getAllPointsOfInterest() {
        return poiRepository.findAll();
    }

    // Récupérer un POI par ID
    public Optional<PointOfInterest> getPointOfInterestById(String id) {
        return poiRepository.findById(id);
    }

    // Récupérer les POI par ville (nom)
    public List<PointOfInterest> getPointsOfInterestByCity(String cityName) {
        return poiRepository.findByCityNameIgnoreCase(cityName);
    }

    // Récupérer les POI par ville (ID)
    public List<PointOfInterest> getPointsOfInterestByCityId(Long cityId) {
        return poiRepository.findByCityId(cityId);
    }

    // Récupérer les POI par type
    public List<PointOfInterest> getPointsOfInterestByType(String type) {
        return poiRepository.findByTypeIgnoreCase(type);
    }

    // Récupérer les POI par ville et type
    public List<PointOfInterest> getPointsOfInterestByCityAndType(String cityName, String type) {
        return poiRepository.findByCityNameIgnoreCaseAndTypeIgnoreCase(cityName, type);
    }

    // Rechercher par nom (autocomplétion)
    public List<PointOfInterest> searchPointsOfInterestByName(String name) {
        return poiRepository.findByNameContainingIgnoreCase(name);
    }

    // Mettre à jour un POI
    public PointOfInterest updatePointOfInterest(String id, PointOfInterestDto poiDto) {
        return poiRepository.findById(id).map(existingPoi -> {
            existingPoi.setName(poiDto.getName());
            existingPoi.setCityName(poiDto.getCityName());
            existingPoi.setCityId(poiDto.getCityId());
            existingPoi.setType(poiDto.getType());
            existingPoi.setDescription(poiDto.getDescription());
            existingPoi.setGeographicInfo(poiDto.getGeographicInfo());
            return poiRepository.save(existingPoi);
        }).orElseThrow(() -> new RuntimeException("Point of Interest not found with id: " + id));
    }

    // Supprimer un POI
    public void deletePointOfInterest(String id) {
        if (!poiRepository.existsById(id)) {
            throw new RuntimeException("Point of Interest not found with id: " + id);
        }
        poiRepository.deleteById(id);
    }

    // Convertir entité vers DTO
    public PointOfInterestDto convertToDto(PointOfInterest poi) {
        PointOfInterestDto dto = new PointOfInterestDto();
        dto.setId(poi.getId());
        dto.setName(poi.getName());
        dto.setCityName(poi.getCityName());
        dto.setCityId(poi.getCityId());
        dto.setType(poi.getType());
        dto.setDescription(poi.getDescription());
        dto.setGeographicInfo(poi.getGeographicInfo());
        return dto;
    }
}