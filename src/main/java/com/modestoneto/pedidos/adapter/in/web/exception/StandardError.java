package com.modestoneto.pedidos.adapter.in.web.exception;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

public record StandardError(
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT") Instant timestamp,
		Integer status,
		String error,
		String message,
		String path) {
}
