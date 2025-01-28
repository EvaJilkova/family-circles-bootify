package cz.familycircles.family_circles_bootify.service;

import cz.familycircles.family_circles_bootify.domain.Country;
import cz.familycircles.family_circles_bootify.domain.Region;
import cz.familycircles.family_circles_bootify.model.RegionDTO;
import cz.familycircles.family_circles_bootify.repos.CountryRepository;
import cz.familycircles.family_circles_bootify.repos.RegionRepository;
import cz.familycircles.family_circles_bootify.util.NotFoundException;
import cz.familycircles.family_circles_bootify.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class RegionService {

    private final RegionRepository regionRepository;
    private final CountryRepository countryRepository;

    public RegionService(final RegionRepository regionRepository,
            final CountryRepository countryRepository) {
        this.regionRepository = regionRepository;
        this.countryRepository = countryRepository;
    }

    public List<RegionDTO> findAll() {
        final List<Region> regions = regionRepository.findAll(Sort.by("regionId"));
        return regions.stream()
                .map(region -> mapToDTO(region, new RegionDTO()))
                .toList();
    }

    public RegionDTO get(final UUID regionId) {
        return regionRepository.findById(regionId)
                .map(region -> mapToDTO(region, new RegionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final RegionDTO regionDTO) {
        final Region region = new Region();
        mapToEntity(regionDTO, region);
        return regionRepository.save(region).getRegionId();
    }

    public void update(final UUID regionId, final RegionDTO regionDTO) {
        final Region region = regionRepository.findById(regionId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(regionDTO, region);
        regionRepository.save(region);
    }

    public void delete(final UUID regionId) {
        regionRepository.deleteById(regionId);
    }

    private RegionDTO mapToDTO(final Region region, final RegionDTO regionDTO) {
        regionDTO.setRegionId(region.getRegionId());
        regionDTO.setRegionName(region.getRegionName());
        regionDTO.setRegionCode(region.getRegionCode());
        return regionDTO;
    }

    private Region mapToEntity(final RegionDTO regionDTO, final Region region) {
        region.setRegionName(regionDTO.getRegionName());
        region.setRegionCode(regionDTO.getRegionCode());
        return region;
    }

    public ReferencedWarning getReferencedWarning(final UUID regionId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Region region = regionRepository.findById(regionId)
                .orElseThrow(NotFoundException::new);
        final Country regionIdCountry = countryRepository.findFirstByRegionId(region);
        if (regionIdCountry != null) {
            referencedWarning.setKey("region.country.regionId.referenced");
            referencedWarning.addParam(regionIdCountry.getCountryId());
            return referencedWarning;
        }
        return null;
    }

}
