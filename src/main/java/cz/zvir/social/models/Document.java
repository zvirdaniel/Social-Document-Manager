package cz.zvir.social.models;

import cz.zvir.social.models.base.CommonModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

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

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "documents")
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
}
