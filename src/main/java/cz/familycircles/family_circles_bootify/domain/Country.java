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
public class Country {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "char(36)")
    @GeneratedValue
    @UuidGenerator
    private UUID countryId;

    @Column(nullable = false, unique = true)
    private String countryName;

    @Column(length = 2)
    private String countryCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id_id")
    private Region regionId;

    @OneToMany(mappedBy = "countryId")
    private Set<Location> countryIdLocations;

}
