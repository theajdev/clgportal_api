package com.aj.clgportal.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserNameNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String username;
	public UserNameNotFoundException(String username) {
		super(String.format("%s", username));
		this.username = username;
	}
}
