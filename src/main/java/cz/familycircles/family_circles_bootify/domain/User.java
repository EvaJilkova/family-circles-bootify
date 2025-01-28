package cz.familycircles.family_circles_bootify.domain;

import cz.familycircles.family_circles_bootify.model.LanguageEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;


@Entity
@Getter
@Setter
public class User {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "char(36)")
    @GeneratedValue
    @UuidGenerator
    private UUID userId;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String phoneNumber;

    @Column
    @Enumerated(EnumType.STRING)
    private LanguageEnum language;

    @OneToMany(mappedBy = "userId")
    private Set<Actions> userIdActiones;

    @OneToMany(mappedBy = "userId")
    private Set<Inventory> userIdInventories;

}
