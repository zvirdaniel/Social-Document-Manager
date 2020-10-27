package cz.zvir.social.specifications;

import cz.zvir.social.models.Document;
import cz.zvir.social.models.Like;
import cz.zvir.social.specifications.base.CommonSpecification;
import cz.zvir.social.specifications.base.CommonSpecificationMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.criteria.JoinType;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DocumentSpecification {
	public static final CommonSpecificationMapper<Like, Document> mapperToLike = it -> it.join("likes", JoinType.LEFT);

	public static CommonSpecification<Document> likedByUser(final long userId) {
		return LikeSpecifications.byUser(userId).map(mapperToLike);
	}
}
