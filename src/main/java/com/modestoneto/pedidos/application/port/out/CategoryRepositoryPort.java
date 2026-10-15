package com.modestoneto.pedidos.application.port.out;

import java.util.List;
import java.util.Optional;

import com.modestoneto.pedidos.domain.model.Category;

public interface CategoryRepositoryPort {

	List<Category> findAll();

	Optional<Category> findById(Long id);

	Category save(Category category);
}
