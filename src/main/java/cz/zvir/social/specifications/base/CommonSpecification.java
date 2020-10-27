package cz.zvir.social.specifications.base;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

@FunctionalInterface
public interface CommonSpecification <T> {
	Predicate toPredicate(From<?, T> root, CriteriaQuery<?> query, CriteriaBuilder builder);

	default <Z> CommonSpecification<Z> map(CommonSpecificationMapper<T, Z> mapper) {
		return (root, query, builder) -> toPredicate(mapper.map(root), query, builder);
	}

	default Specification<T> asJpaSpecification() {
		return this::toPredicate;
	}
}
