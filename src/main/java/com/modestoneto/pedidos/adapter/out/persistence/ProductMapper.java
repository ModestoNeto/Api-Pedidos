package com.modestoneto.pedidos.adapter.out.persistence;

import org.springframework.stereotype.Component;

import com.modestoneto.pedidos.domain.model.Category;
import com.modestoneto.pedidos.domain.model.Product;

@Component
public class ProductMapper {

	private final CategoryMapper categoryMapper;

	public ProductMapper(CategoryMapper categoryMapper) {
		this.categoryMapper = categoryMapper;
	}

	public Product toDomain(ProductJpaEntity entity) {
		Product product = new Product(entity.getId(), entity.getName(), entity.getDescription(), entity.getPrice(),
				entity.getImgUrl());
		for (CategoryJpaEntity category : entity.getCategories()) {
			product.getCategories().add(categoryMapper.toDomain(category));
		}
		return product;
	}

	public ProductJpaEntity toEntity(Product product) {
		ProductJpaEntity entity = new ProductJpaEntity(product.getId(), product.getName(), product.getDescription(),
				product.getPrice(), product.getImgUrl());
		for (Category category : product.getCategories()) {
			entity.getCategories().add(categoryMapper.toEntity(category));
		}
		return entity;
	}
}
