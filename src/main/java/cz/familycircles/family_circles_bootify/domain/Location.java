package cz.familycircles.family_circles_bootify.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;


@Entity
@Getter
@Setter
public class Location {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "char(36)")
    @GeneratedValue
    @UuidGenerator
    private UUID locationId;

    @Column
    private String street;

    @Column
    private String streetNumber;

    @Column
    private String postalCode;

    @Column
    private String city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id_id")
    private Country countryId;

    @OneToMany(mappedBy = "locationId")
    private Set<Actions> locationIdActionses;

}
