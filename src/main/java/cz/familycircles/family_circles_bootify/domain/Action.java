package cz.familycircles.family_circles_bootify.domain;

import cz.familycircles.family_circles_bootify.model.ActionTypeEnum;
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
public class Action {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "char(36)")
    @GeneratedValue
    @UuidGenerator
    private UUID actionId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "\"description\"")
    private String description;

    @Column
    @Enumerated(EnumType.STRING)
    private ActionTypeEnum actionType;

    @OneToMany(mappedBy = "actionId")
    private Set<Actions> actionIdActionses;

}
