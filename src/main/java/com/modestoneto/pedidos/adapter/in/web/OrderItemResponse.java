package com.modestoneto.pedidos.adapter.in.web;

public record OrderItemResponse(Long productId, String name, Double price, Integer quantity, Double subTotal) {
}
