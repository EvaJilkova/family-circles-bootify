package cz.familycircles.family_circles_bootify.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegionDTO {

    private UUID regionId;

    @Size(max = 255)
    private String regionName;

    @NotNull
    @Size(max = 2)
    private String regionCode;

}
