package cz.familycircles.family_circles_bootify.service;

import cz.familycircles.family_circles_bootify.domain.Actions;
import cz.familycircles.family_circles_bootify.domain.Country;
import cz.familycircles.family_circles_bootify.domain.Location;
import cz.familycircles.family_circles_bootify.model.LocationDTO;
import cz.familycircles.family_circles_bootify.repos.ActionsRepository;
import cz.familycircles.family_circles_bootify.repos.CountryRepository;
import cz.familycircles.family_circles_bootify.repos.LocationRepository;
import cz.familycircles.family_circles_bootify.util.NotFoundException;
import cz.familycircles.family_circles_bootify.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final CountryRepository countryRepository;
    private final ActionsRepository actionsRepository;

    public LocationService(final LocationRepository locationRepository,
            final CountryRepository countryRepository, final ActionsRepository actionsRepository) {
        this.locationRepository = locationRepository;
        this.countryRepository = countryRepository;
        this.actionsRepository = actionsRepository;
    }

    public List<LocationDTO> findAll() {
        final List<Location> locations = locationRepository.findAll(Sort.by("locationId"));
        return locations.stream()
                .map(location -> mapToDTO(location, new LocationDTO()))
                .toList();
    }

    public LocationDTO get(final UUID locationId) {
        return locationRepository.findById(locationId)
                .map(location -> mapToDTO(location, new LocationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final LocationDTO locationDTO) {
        final Location location = new Location();
        mapToEntity(locationDTO, location);
        return locationRepository.save(location).getLocationId();
    }

    public void update(final UUID locationId, final LocationDTO locationDTO) {
        final Location location = locationRepository.findById(locationId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(locationDTO, location);
        locationRepository.save(location);
    }

    public void delete(final UUID locationId) {
        locationRepository.deleteById(locationId);
    }

    private LocationDTO mapToDTO(final Location location, final LocationDTO locationDTO) {
        locationDTO.setLocationId(location.getLocationId());
        locationDTO.setStreet(location.getStreet());
        locationDTO.setStreetNumber(location.getStreetNumber());
        locationDTO.setPostalCode(location.getPostalCode());
        locationDTO.setCity(location.getCity());
        locationDTO.setCountryId(location.getCountryId() == null ? null : location.getCountryId().getCountryId());
        return locationDTO;
    }

    private Location mapToEntity(final LocationDTO locationDTO, final Location location) {
        location.setStreet(locationDTO.getStreet());
        location.setStreetNumber(locationDTO.getStreetNumber());
        location.setPostalCode(locationDTO.getPostalCode());
        location.setCity(locationDTO.getCity());
        final Country countryId = locationDTO.getCountryId() == null ? null : countryRepository.findById(locationDTO.getCountryId())
                .orElseThrow(() -> new NotFoundException("countryId not found"));
        location.setCountryId(countryId);
        return location;
    }

    public ReferencedWarning getReferencedWarning(final UUID locationId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Location location = locationRepository.findById(locationId)
                .orElseThrow(NotFoundException::new);
        final Actions locationIdActions = actionsRepository.findFirstByLocationId(location);
        if (locationIdActions != null) {
            referencedWarning.setKey("location.actions.locationId.referenced");
            referencedWarning.addParam(locationIdActions.getActionsId());
            return referencedWarning;
        }
        return null;
    }

}
