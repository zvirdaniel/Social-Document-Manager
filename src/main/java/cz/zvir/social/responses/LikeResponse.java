package cz.zvir.social.responses;

import cz.zvir.social.models.Like;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class LikeResponse {
	private Long id;
	private OffsetDateTime createdAt;
	private UserResponse user;

	public LikeResponse(final Like like) {
		this.id = like.getId();
		this.createdAt = like.getCreatedAt();
		this.user = new UserResponse(like.getUser());
	}
}
