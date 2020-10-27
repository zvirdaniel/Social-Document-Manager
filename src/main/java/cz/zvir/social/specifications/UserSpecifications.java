package cz.zvir.social.specifications;

import cz.zvir.social.models.User;
import cz.zvir.social.specifications.base.CommonSpecification;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserSpecifications {
	public static CommonSpecification<User> byId(final long id) {
		return (root, query, builder) -> builder.equal(root.get("id"), id);
	}
}
