package com.preludio.services.infra.auth.exception;

@SuppressWarnings("serial")
public class LoginException extends RuntimeException {

	public LoginException(String message) {
		super(message);
	}

	public LoginException(Throwable cause) {
		super(cause);
	}

}
