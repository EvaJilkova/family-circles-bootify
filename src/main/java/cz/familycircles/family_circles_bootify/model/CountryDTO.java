package cz.familycircles.family_circles_bootify.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CountryDTO {

    private UUID countryId;

    @NotNull
    @Size(max = 255)
    @CountryCountryNameUnique
    private String countryName;

    @Size(max = 2)
    private String countryCode;

    private UUID regionId;

}
