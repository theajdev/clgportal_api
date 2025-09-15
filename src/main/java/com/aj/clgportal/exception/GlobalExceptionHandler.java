package com.aj.clgportal.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.aj.clgportal.dto.ApiResponse;
import com.aj.clgportal.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
		String message = ex.getMessage();
		ApiResponse apiResponse = new ApiResponse(message, false);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserNameNotFoundException.class)
	public ResponseEntity<ErrorResponse> UserNameNotFoundExceptionHandler(UserNameNotFoundException ex) {
		String message = ex.getMessage();
		ErrorResponse errorResponse = new ErrorResponse(message, false);
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleMethodArgumentsNotValidException(
			MethodArgumentNotValidException ex) {
		Map<String, String> resp = new HashMap<>();

		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			resp.put(fieldName, message);
		});
		return new ResponseEntity<Map<String, String>>(resp, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(RoleNameExistsException.class)
	public ResponseEntity<Map<String,Object>> handleRoleNameExistsException(RoleNameExistsException ex){
		Map<String, Object> map=new HashMap<>();
		map.put("error", "User type already exists.");
		map.put("message", ex.getMessage());
		map.put("status", HttpStatus.CONFLICT);
		
		return new ResponseEntity<>(map,HttpStatus.CONFLICT);
	}
}
