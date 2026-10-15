package com.modestoneto.pedidos.adapter.out.persistence;

import org.springframework.stereotype.Component;

import com.modestoneto.pedidos.domain.model.Category;

@Component
public class CategoryMapper {

	public Category toDomain(CategoryJpaEntity entity) {
		return new Category(entity.getId(), entity.getName());
	}

	public CategoryJpaEntity toEntity(Category category) {
		return new CategoryJpaEntity(category.getId(), category.getName());
	}
}
