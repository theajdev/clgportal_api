package com.aj.clgportal.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleNameExistsException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
	public RoleNameExistsException(String message) {
		super(String.format("%s", message));
		this.message=message;
		
	}

}
