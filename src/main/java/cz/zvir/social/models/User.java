package cz.zvir.social.models;

import com.sun.istack.NotNull;
import cz.zvir.social.models.base.CommonModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User extends CommonModel<Long> {
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(nullable = false)
	private String name;

	@OneToMany(orphanRemoval = true)
	private List<Like> likes = new ArrayList<>();

	@OneToMany(orphanRemoval = true)
	private List<Document> documents = new ArrayList<>();

	public void setLikes(List<Like> likes) {
		this.likes.clear();
		Optional.ofNullable(likes).ifPresent(this.likes::addAll);
	}

	public void setDocuments(List<Document> documents) {
		this.documents.clear();
		Optional.ofNullable(documents).ifPresent(this.documents::addAll);
	}

	public User(final String name) {
		this.name = name;
	}
}
