package cz.familycircles.family_circles_bootify.model;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ActionsDTO {

    private UUID actionsId;
    private UUID actionId;
    private UUID locationId;
    private UUID userId;

}
