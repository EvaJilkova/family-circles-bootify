package cz.familycircles.family_circles_bootify.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDTO {

    private UUID userId;

    @NotNull
    @Size(max = 255)
    @UserUserNameUnique
    private String userName;

    @Size(max = 255)
    private String firstName;

    @Size(max = 255)
    private String lastName;

    @NotNull
    @Size(max = 255)
    @UserEmailUnique
    private String email;

    @Size(max = 255)
    private String phoneNumber;

    private LanguageEnum language;

}
