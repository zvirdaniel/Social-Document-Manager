package cz.zvir.social.exceptions;

public class SocialException extends RuntimeException {
	public SocialException() {
	}

	public SocialException(String message) {
		super(message);
	}

	public SocialException(String message, Throwable cause) {
		super(message, cause);
	}

	public SocialException(Throwable cause) {
		super(cause);
	}

	public SocialException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
