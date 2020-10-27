package cz.zvir.social.requests;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRequest {
	@NotNull
	private String content;

	@NotNull
	private Long user;
}
