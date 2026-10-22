package com.modestoneto.pedidos.adapter.in.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.modestoneto.pedidos.adapter.in.web.exception.StandardError;
import com.modestoneto.pedidos.application.port.in.FindProductByIdUseCase;
import com.modestoneto.pedidos.application.port.in.FindProductsUseCase;
import com.modestoneto.pedidos.domain.model.Product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Produtos", description = "Consulta de produtos, com suas categorias")
@RestController
@RequestMapping("/products")
public class ProductController {

	private final FindProductsUseCase findProducts;
	private final FindProductByIdUseCase findProductById;

	public ProductController(FindProductsUseCase findProducts, FindProductByIdUseCase findProductById) {
		this.findProducts = findProducts;
		this.findProductById = findProductById;
	}

	@Operation(summary = "Lista todos os produtos")
	@GetMapping
	public ResponseEntity<List<ProductResponse>> findAll() {
		List<ProductResponse> list = findProducts.findAll().stream().map(this::toResponse).toList();
		return ResponseEntity.ok(list);
	}

	@Operation(summary = "Busca um produto por id", description = "Traz as categorias do produto.")
	@ApiResponse(responseCode = "400", description = "Id com formato inválido",
			content = @Content(schema = @Schema(implementation = StandardError.class)))
	@ApiResponse(responseCode = "404", description = "Produto não encontrado",
			content = @Content(schema = @Schema(implementation = StandardError.class)))
	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
		return ResponseEntity.ok(toResponse(findProductById.findById(id)));
	}

	private ProductResponse toResponse(Product product) {
		List<CategoryResponse> categories = product.getCategories().stream()
				.map(category -> new CategoryResponse(category.getId(), category.getName())).toList();
		return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice(),
				product.getImgUrl(), categories);
	}
}
