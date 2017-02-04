package com.ibm.interac.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class InteracExceptionHandler extends ResponseEntityExceptionHandler
{
	private static final Logger logger = LoggerFactory.getLogger(InteracExceptionHandler.class);

	@ExceptionHandler(value = { IllegalArgumentException.class })
	protected ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request)
	{
		logger.error(ex.getMessage(), ex);

		ResponseEntity<String> response = ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
				.body(ex.getMessage());
		return response;
	}

	@ExceptionHandler(value = { Exception.class })
	protected ResponseEntity<?> handleGenericException(Exception ex, WebRequest request)
	{
		logger.error(ex.getMessage(), ex);

		ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.contentType(MediaType.APPLICATION_JSON).body(ex.getMessage());
		return response;
	}

}
