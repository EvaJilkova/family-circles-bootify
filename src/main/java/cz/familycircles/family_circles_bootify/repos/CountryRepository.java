package cz.familycircles.family_circles_bootify.repos;

import cz.familycircles.family_circles_bootify.domain.Country;
import cz.familycircles.family_circles_bootify.domain.Region;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CountryRepository extends JpaRepository<Country, UUID> {

    Country findFirstByRegionId(Region region);

    boolean existsByCountryNameIgnoreCase(String countryName);

}
