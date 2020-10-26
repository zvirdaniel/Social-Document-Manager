package cz.zvir.social.requests;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class DocumentRequest {
	@NotNull
	private String content;

	@NotNull
	private Long user;
}
