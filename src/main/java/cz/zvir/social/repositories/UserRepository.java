package cz.zvir.social.repositories;

import cz.zvir.social.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
