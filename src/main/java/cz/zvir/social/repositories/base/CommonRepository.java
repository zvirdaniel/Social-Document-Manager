package cz.zvir.social.repositories.base;

import cz.zvir.social.specifications.base.CommonSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface CommonRepository <T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
	default Optional<T> findOne(CommonSpecification<T> spec) {
		return findOne(spec.asJpaSpecification());
	}

	default List<T> findAll(CommonSpecification<T> spec) {
		return findAll(spec.asJpaSpecification());
	}

	default Page<T> findAll(CommonSpecification<T> spec, Pageable pageable) {
		return findAll(spec.asJpaSpecification(), pageable);
	}

	default List<T> findAll(CommonSpecification<T> spec, Sort sort) {
		return findAll(spec.asJpaSpecification(), sort);
	}

	default long count(CommonSpecification<T> spec) {
		return count(spec.asJpaSpecification());
	}
}
