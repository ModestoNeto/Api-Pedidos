package com.modestoneto.pedidos.adapter.in.web;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public record OrderResponse(
		Long id,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT") Instant moment,
		String orderStatus,
		UserResponse client,
		List<OrderItemResponse> items,
		PaymentResponse payment,
		Double total) {
}
