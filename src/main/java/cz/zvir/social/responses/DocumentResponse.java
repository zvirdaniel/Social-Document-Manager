package cz.zvir.social.responses;

import cz.zvir.social.models.Document;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class DocumentResponse {
	private String id;
	private OffsetDateTime createdAt;
	private String content;
	private UserResponse user;
	private List<LikeResponse> likes;
	private long likeCount;

	public DocumentResponse(final Document document) {
		this.id = document.getId();
		this.createdAt = document.getCreatedAt();
		this.content = document.getContent();
		this.user = new UserResponse(document.getUser());
		this.likes = document.getLikes().stream().map(LikeResponse::new).collect(Collectors.toList());
		this.likeCount = this.likes.size();
	}
}
