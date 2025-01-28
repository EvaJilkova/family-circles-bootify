package cz.familycircles.family_circles_bootify.model;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class InventoryDTO {

    private UUID inventoryId;
    private UUID itemId;
    private UUID userId;

}
