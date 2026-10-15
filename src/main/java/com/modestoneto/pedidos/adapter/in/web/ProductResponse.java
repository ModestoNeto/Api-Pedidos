package com.modestoneto.pedidos.adapter.in.web;

import java.util.List;

public record ProductResponse(Long id, String name, String description, Double price, String imgUrl,
		List<CategoryResponse> categories) {
}
