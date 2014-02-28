package com.preludio.services.infra.auth.exception;

@SuppressWarnings("serial")
public class AuthenticationException extends RuntimeException {

	public AuthenticationException(String message) {
		super(message);
	}

	public AuthenticationException(Throwable cause) {
		super(cause);
	}

}
