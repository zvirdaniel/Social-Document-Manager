package cz.zvir.social.repositories;

import cz.zvir.social.models.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
	List<Like> findAllByUser_Id(long userId);
}
