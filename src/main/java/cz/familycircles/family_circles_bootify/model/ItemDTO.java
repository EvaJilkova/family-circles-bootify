package cz.familycircles.family_circles_bootify.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ItemDTO {

    private UUID itemId;

    @NotNull
    @Size(max = 255)
    @ItemItemNameUnique
    private String itemName;

    private UUID categoryId;

}
