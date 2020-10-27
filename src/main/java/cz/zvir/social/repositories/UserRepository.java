package cz.zvir.social.repositories;

import cz.zvir.social.models.User;
import cz.zvir.social.repositories.base.CommonRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CommonRepository<User, Long> {
}
