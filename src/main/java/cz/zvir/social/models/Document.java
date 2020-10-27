package cz.zvir.social.models;

import cz.zvir.social.models.base.CommonModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Entity
@Table(name = "documents")
@NoArgsConstructor
public class Document extends CommonModel<String> {
	@Id
	@Column(nullable = false)
	private String id;

	@Column(nullable = false, length = 2000)
	private String content;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private User user;

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Like> likes = new ArrayList<>();

	public void setLikes(List<Like> likes) {
		this.likes.clear();
		Optional.ofNullable(likes).ifPresent(this.likes::addAll);
	}

	public Document(final String id, final String content, final User user) {
		this.id = id;
		this.content = content;
		this.user = user;
	}
}
