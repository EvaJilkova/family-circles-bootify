package cz.familycircles.family_circles_bootify.repos;

import cz.familycircles.family_circles_bootify.domain.Category;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, UUID> {

    boolean existsByCategoryNameIgnoreCase(String categoryName);

}
