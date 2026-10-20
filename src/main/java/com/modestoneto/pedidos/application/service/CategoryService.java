package com.modestoneto.pedidos.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.modestoneto.pedidos.application.port.in.FindCategoriesUseCase;
import com.modestoneto.pedidos.application.port.in.FindCategoryByIdUseCase;
import com.modestoneto.pedidos.application.port.out.CategoryRepositoryPort;
import com.modestoneto.pedidos.domain.exception.ResourceNotFoundException;
import com.modestoneto.pedidos.domain.model.Category;

@Service
public class CategoryService implements FindCategoriesUseCase, FindCategoryByIdUseCase {

	private final CategoryRepositoryPort categoryRepository;

	public CategoryService(CategoryRepositoryPort categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	@Override
	public Category findById(Long id) {
		return categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada: id " + id));
	}
}
