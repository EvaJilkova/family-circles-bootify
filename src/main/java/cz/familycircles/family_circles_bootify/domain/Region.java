package cz.familycircles.family_circles_bootify.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Region {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "char(36)")
    @GeneratedValue
    @UuidGenerator
    private UUID regionId;

    @Column
    private String regionName;

    @Column(nullable = false, length = 2)
    private String regionCode;

    @OneToMany(mappedBy = "regionId")
    private Set<Country> regionIdCountries;

}
