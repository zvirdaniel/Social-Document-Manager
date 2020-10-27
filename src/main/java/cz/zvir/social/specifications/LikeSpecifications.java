package cz.zvir.social.specifications;

import cz.zvir.social.models.Like;
import cz.zvir.social.models.User;
import cz.zvir.social.specifications.base.CommonSpecification;
import cz.zvir.social.specifications.base.CommonSpecificationMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.criteria.JoinType;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LikeSpecifications {
	public static final CommonSpecificationMapper<User, Like> mapperToUser = it -> it.join("user", JoinType.LEFT);

	public static CommonSpecification<Like> byUser(final long userId) {
		return UserSpecifications.byId(userId).map(mapperToUser);
	}
}
