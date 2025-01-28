package cz.familycircles.family_circles_bootify.repos;

import cz.familycircles.family_circles_bootify.domain.CategoryType;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryTypeRepository extends JpaRepository<CategoryType, UUID> {

    boolean existsByNameIgnoreCase(String name);

}
