package com.availabilityslot.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = ResourceNotFoundException.class)
	public ResponseEntity<Object> handlerUserException(ResourceNotFoundException uex, WebRequest request) {

		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", HttpStatus.NOT_FOUND.value());
		body.put("error", "User Not Found");
		body.put("message", uex.getMessage());
		body.put("path", request.getDescription(false));

		return new ResponseEntity<Object>(body, HttpStatus.NOT_FOUND);
	}
	
	  @ExceptionHandler(Exception.class)
	    public ResponseEntity<Object> handleAllOtherExceptions(
	            Exception ex, WebRequest request) {

	        Map<String, Object> body = new HashMap<>();
	        body.put("timestamp", LocalDateTime.now());
	        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
	        body.put("error", "Internal Server Error");
	        body.put("message", ex.getMessage());
	        body.put("details", request.getDescription(false));

	        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	    }	

}
