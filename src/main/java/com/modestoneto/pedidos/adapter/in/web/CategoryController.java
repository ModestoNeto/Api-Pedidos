package com.modestoneto.pedidos.adapter.in.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.modestoneto.pedidos.application.port.in.FindCategoriesUseCase;
import com.modestoneto.pedidos.application.port.in.FindCategoryByIdUseCase;
import com.modestoneto.pedidos.domain.model.Category;

@RestController
@RequestMapping("/categories")
public class CategoryController {

	private final FindCategoriesUseCase findCategories;
	private final FindCategoryByIdUseCase findCategoryById;

	public CategoryController(FindCategoriesUseCase findCategories, FindCategoryByIdUseCase findCategoryById) {
		this.findCategories = findCategories;
		this.findCategoryById = findCategoryById;
	}

	@GetMapping
	public ResponseEntity<List<CategoryResponse>> findAll() {
		List<CategoryResponse> list = findCategories.findAll().stream().map(this::toResponse).toList();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoryResponse> findById(@PathVariable Long id) {
		return ResponseEntity.ok(toResponse(findCategoryById.findById(id)));
	}

	private CategoryResponse toResponse(Category category) {
		return new CategoryResponse(category.getId(), category.getName());
	}
}
