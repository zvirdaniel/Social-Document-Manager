package cz.zvir.social.specifications.base;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;

@FunctionalInterface
public interface CommonSpecificationMapper <SOURCE, DESTINATION> {
	Join<DESTINATION, SOURCE> map(From<?, DESTINATION> it);
}
