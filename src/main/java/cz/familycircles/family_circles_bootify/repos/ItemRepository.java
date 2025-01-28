package cz.familycircles.family_circles_bootify.repos;

import cz.familycircles.family_circles_bootify.domain.Category;
import cz.familycircles.family_circles_bootify.domain.Item;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ItemRepository extends JpaRepository<Item, UUID> {

    Item findFirstByCategoryId(Category category);

    boolean existsByItemNameIgnoreCase(String itemName);

}
