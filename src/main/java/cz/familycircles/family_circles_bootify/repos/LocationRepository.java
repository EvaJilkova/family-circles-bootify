package cz.familycircles.family_circles_bootify.repos;

import cz.familycircles.family_circles_bootify.domain.Country;
import cz.familycircles.family_circles_bootify.domain.Location;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LocationRepository extends JpaRepository<Location, UUID> {

    Location findFirstByCountryId(Country country);

}
