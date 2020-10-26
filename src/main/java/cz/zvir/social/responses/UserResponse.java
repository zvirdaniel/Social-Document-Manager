package cz.zvir.social.responses;

import cz.zvir.social.models.User;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class UserResponse {
	private Long id;
	private OffsetDateTime createdAt;
	private String name;

	public UserResponse(final User user) {
		this.id = user.getId();
		this.createdAt = user.getCreatedAt();
		this.name = user.getName();
	}
}
