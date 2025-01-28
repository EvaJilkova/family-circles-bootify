package cz.familycircles.family_circles_bootify.repos;

import cz.familycircles.family_circles_bootify.domain.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByUserNameIgnoreCase(String userName);

    boolean existsByEmailIgnoreCase(String email);

}
