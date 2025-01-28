package cz.familycircles.family_circles_bootify.model;

import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LocationDTO {

    private UUID locationId;

    @Size(max = 255)
    private String street;

    @Size(max = 255)
    private String streetNumber;

    @Size(max = 255)
    private String postalCode;

    @Size(max = 255)
    private String city;

    private UUID countryId;

}
