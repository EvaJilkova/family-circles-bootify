package cz.familycircles.family_circles_bootify.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;


@Entity
@Getter
@Setter
public class Actions {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "char(36)")
    @GeneratedValue
    @UuidGenerator
    private UUID actionsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "action_id_id")
    private Action actionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id_id")
    private Location locationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id_id")
    private User userId;

}
