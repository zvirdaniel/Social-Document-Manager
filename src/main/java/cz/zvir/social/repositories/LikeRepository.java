package cz.zvir.social.repositories;

import cz.zvir.social.models.Like;
import cz.zvir.social.repositories.base.CommonRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends CommonRepository<Like, Long> {
}
