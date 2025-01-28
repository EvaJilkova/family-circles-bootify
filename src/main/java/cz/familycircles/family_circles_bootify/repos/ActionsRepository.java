package cz.familycircles.family_circles_bootify.repos;

import cz.familycircles.family_circles_bootify.domain.Action;
import cz.familycircles.family_circles_bootify.domain.Actions;
import cz.familycircles.family_circles_bootify.domain.Location;
import cz.familycircles.family_circles_bootify.domain.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ActionsRepository extends JpaRepository<Actions, UUID> {

    Actions findFirstByActionId(Action action);

    Actions findFirstByLocationId(Location location);

    Actions findFirstByUserId(User user);

}
