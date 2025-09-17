package com.aj.clgportal.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DuplicateResourceException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
	public DuplicateResourceException(String message) {
		super(String.format("%s", message));
		this.message=message;
		
	}

}
