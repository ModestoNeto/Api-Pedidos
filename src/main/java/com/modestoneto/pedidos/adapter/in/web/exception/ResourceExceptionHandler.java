package com.modestoneto.pedidos.adapter.in.web.exception;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.modestoneto.pedidos.domain.exception.ConflictException;
import com.modestoneto.pedidos.domain.exception.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
		return build(HttpStatus.NOT_FOUND, "Recurso não encontrado", e.getMessage(), request);
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<StandardError> conflict(ConflictException e, HttpServletRequest request) {
		return build(HttpStatus.CONFLICT, "Conflito", e.getMessage(), request);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
		String message = e.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage())
				.collect(Collectors.joining("; "));
		return build(HttpStatus.BAD_REQUEST, "Erro de validação", message, request);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<StandardError> unreadableBody(HttpMessageNotReadableException e,
			HttpServletRequest request) {
		return build(HttpStatus.BAD_REQUEST, "Requisição inválida", "Corpo da requisição ilegível", request);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<StandardError> typeMismatch(MethodArgumentTypeMismatchException e,
			HttpServletRequest request) {
		return build(HttpStatus.BAD_REQUEST, "Requisição inválida",
				"Valor inválido para o parâmetro '" + e.getName() + "'", request);
	}

	private ResponseEntity<StandardError> build(HttpStatus status, String error, String message,
			HttpServletRequest request) {
		StandardError body = new StandardError(Instant.now(), status.value(), error, message,
				request.getRequestURI());
		return ResponseEntity.status(status).body(body);
	}
}
