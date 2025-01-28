package cz.familycircles.family_circles_bootify.repos;

import cz.familycircles.family_circles_bootify.domain.Region;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RegionRepository extends JpaRepository<Region, UUID> {
}
