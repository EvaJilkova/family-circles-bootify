package cz.familycircles.family_circles_bootify.service;

import cz.familycircles.family_circles_bootify.domain.Country;
import cz.familycircles.family_circles_bootify.domain.Location;
import cz.familycircles.family_circles_bootify.domain.Region;
import cz.familycircles.family_circles_bootify.model.CountryDTO;
import cz.familycircles.family_circles_bootify.repos.CountryRepository;
import cz.familycircles.family_circles_bootify.repos.LocationRepository;
import cz.familycircles.family_circles_bootify.repos.RegionRepository;
import cz.familycircles.family_circles_bootify.util.NotFoundException;
import cz.familycircles.family_circles_bootify.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CountryService {

    private final CountryRepository countryRepository;
    private final RegionRepository regionRepository;
    private final LocationRepository locationRepository;

    public CountryService(final CountryRepository countryRepository,
            final RegionRepository regionRepository, final LocationRepository locationRepository) {
        this.countryRepository = countryRepository;
        this.regionRepository = regionRepository;
        this.locationRepository = locationRepository;
    }

    public List<CountryDTO> findAll() {
        final List<Country> countries = countryRepository.findAll(Sort.by("countryId"));
        return countries.stream()
                .map(country -> mapToDTO(country, new CountryDTO()))
                .toList();
    }

    public CountryDTO get(final UUID countryId) {
        return countryRepository.findById(countryId)
                .map(country -> mapToDTO(country, new CountryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final CountryDTO countryDTO) {
        final Country country = new Country();
        mapToEntity(countryDTO, country);
        return countryRepository.save(country).getCountryId();
    }

    public void update(final UUID countryId, final CountryDTO countryDTO) {
        final Country country = countryRepository.findById(countryId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(countryDTO, country);
        countryRepository.save(country);
    }

    public void delete(final UUID countryId) {
        countryRepository.deleteById(countryId);
    }

    private CountryDTO mapToDTO(final Country country, final CountryDTO countryDTO) {
        countryDTO.setCountryId(country.getCountryId());
        countryDTO.setCountryName(country.getCountryName());
        countryDTO.setCountryCode(country.getCountryCode());
        countryDTO.setRegionId(country.getRegionId() == null ? null : country.getRegionId().getRegionId());
        return countryDTO;
    }

    private Country mapToEntity(final CountryDTO countryDTO, final Country country) {
        country.setCountryName(countryDTO.getCountryName());
        country.setCountryCode(countryDTO.getCountryCode());
        final Region regionId = countryDTO.getRegionId() == null ? null : regionRepository.findById(countryDTO.getRegionId())
                .orElseThrow(() -> new NotFoundException("regionId not found"));
        country.setRegionId(regionId);
        return country;
    }

    public boolean countryNameExists(final String countryName) {
        return countryRepository.existsByCountryNameIgnoreCase(countryName);
    }

    public ReferencedWarning getReferencedWarning(final UUID countryId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Country country = countryRepository.findById(countryId)
                .orElseThrow(NotFoundException::new);
        final Location countryIdLocation = locationRepository.findFirstByCountryId(country);
        if (countryIdLocation != null) {
            referencedWarning.setKey("country.location.countryId.referenced");
            referencedWarning.addParam(countryIdLocation.getLocationId());
            return referencedWarning;
        }
        return null;
    }

}
