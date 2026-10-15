package com.modestoneto.pedidos.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.modestoneto.pedidos.application.port.out.CategoryRepositoryPort;
import com.modestoneto.pedidos.domain.model.Category;

@Component
public class CategoryPersistenceAdapter implements CategoryRepositoryPort {

	private final SpringDataCategoryRepository repository;
	private final CategoryMapper mapper;

	public CategoryPersistenceAdapter(SpringDataCategoryRepository repository, CategoryMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	@Override
	public List<Category> findAll() {
		return repository.findAll().stream().map(mapper::toDomain).toList();
	}

	@Override
	public Optional<Category> findById(Long id) {
		return repository.findById(id).map(mapper::toDomain);
	}

	@Override
	public Category save(Category category) {
		CategoryJpaEntity saved = repository.save(mapper.toEntity(category));
		return mapper.toDomain(saved);
	}
}
