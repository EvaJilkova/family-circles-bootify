package cz.familycircles.family_circles_bootify.repos;

import cz.familycircles.family_circles_bootify.domain.Action;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ActionRepository extends JpaRepository<Action, UUID> {

    boolean existsByNameIgnoreCase(String name);

}
