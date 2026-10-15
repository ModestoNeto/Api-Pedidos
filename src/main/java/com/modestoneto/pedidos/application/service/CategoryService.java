package com.modestoneto.pedidos.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.modestoneto.pedidos.application.port.in.FindCategoriesUseCase;
import com.modestoneto.pedidos.application.port.in.FindCategoryByIdUseCase;
import com.modestoneto.pedidos.application.port.out.CategoryRepositoryPort;
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
	public Optional<Category> findById(Long id) {
		return categoryRepository.findById(id);
	}
}
