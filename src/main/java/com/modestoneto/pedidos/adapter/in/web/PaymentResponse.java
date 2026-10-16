package com.modestoneto.pedidos.adapter.in.web;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

public record PaymentResponse(
		Long id,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT") Instant moment) {
}
