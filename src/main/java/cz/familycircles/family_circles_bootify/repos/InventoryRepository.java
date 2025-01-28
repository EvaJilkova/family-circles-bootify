package cz.familycircles.family_circles_bootify.repos;

import cz.familycircles.family_circles_bootify.domain.Inventory;
import cz.familycircles.family_circles_bootify.domain.Item;
import cz.familycircles.family_circles_bootify.domain.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InventoryRepository extends JpaRepository<Inventory, UUID> {

    Inventory findFirstByItemId(Item item);

    Inventory findFirstByUserId(User user);

}
