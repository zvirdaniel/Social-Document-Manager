package cz.zvir.social.models.base;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.time.OffsetDateTime;
import java.util.Objects;

@Getter
@MappedSuperclass
public abstract class CommonModel <ID> {
	@Column(nullable = false, updatable = false)
	private OffsetDateTime createdAt;

	public abstract ID getId();

	@PrePersist
	protected void prePersist() {
		this.createdAt = OffsetDateTime.now();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}
		return Objects.equals(getId(), ((CommonModel<?>) o).getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}

	@Override
	public String toString() {
		return "CommonModel{" +
				"id=" + getId() + ", " +
				"createdAt=" + getCreatedAt() +
				'}';
	}
}
