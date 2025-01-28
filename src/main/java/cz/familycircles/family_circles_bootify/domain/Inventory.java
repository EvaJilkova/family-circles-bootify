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
public class Inventory {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "char(36)")
    @GeneratedValue
    @UuidGenerator
    private UUID inventoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id_id")
    private Item itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id_id")
    private User userId;

}
