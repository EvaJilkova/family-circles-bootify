package cz.familycircles.family_circles_bootify.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ActionDTO {

    private UUID actionId;

    @NotNull
    @Size(max = 255)
    @ActionNameUnique
    private String name;

    @Size(max = 255)
    private String description;

    private ActionTypeEnum actionType;

}
