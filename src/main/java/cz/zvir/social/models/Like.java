package cz.zvir.social.models;

import com.sun.istack.NotNull;
import cz.zvir.social.models.base.CommonModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "likes")
@NoArgsConstructor
public class Like extends CommonModel<Long> {
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Document document;

	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private User user;

	public Like(Document document, User user) {
		this.document = document;
		this.user = user;
	}
}
