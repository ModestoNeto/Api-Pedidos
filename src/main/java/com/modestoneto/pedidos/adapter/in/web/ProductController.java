package com.modestoneto.pedidos.adapter.in.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.modestoneto.pedidos.application.port.in.FindProductByIdUseCase;
import com.modestoneto.pedidos.application.port.in.FindProductsUseCase;
import com.modestoneto.pedidos.domain.model.Product;

@RestController
@RequestMapping("/products")
public class ProductController {

	private final FindProductsUseCase findProducts;
	private final FindProductByIdUseCase findProductById;

	public ProductController(FindProductsUseCase findProducts, FindProductByIdUseCase findProductById) {
		this.findProducts = findProducts;
		this.findProductById = findProductById;
	}

	@GetMapping
	public ResponseEntity<List<ProductResponse>> findAll() {
		List<ProductResponse> list = findProducts.findAll().stream().map(this::toResponse).toList();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
		return findProductById.findById(id)
				.map(product -> ResponseEntity.ok(toResponse(product)))
				.orElse(ResponseEntity.notFound().build());
	}

	private ProductResponse toResponse(Product product) {
		List<CategoryResponse> categories = product.getCategories().stream()
				.map(category -> new CategoryResponse(category.getId(), category.getName())).toList();
		return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice(),
				product.getImgUrl(), categories);
	}
}
