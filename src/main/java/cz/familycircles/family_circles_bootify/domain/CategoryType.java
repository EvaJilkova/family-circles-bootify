package cz.familycircles.family_circles_bootify.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;


@Entity
@Getter
@Setter
public class CategoryType {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "char(36)")
    @GeneratedValue
    @UuidGenerator
    private UUID categoryTypeId;

    @Column(nullable = false, unique = true)
    private String name;

}
