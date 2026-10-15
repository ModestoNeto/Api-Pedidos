package com.modestoneto.pedidos.application.port.in;

import java.util.Optional;

import com.modestoneto.pedidos.domain.model.Category;

public interface FindCategoryByIdUseCase {

	Optional<Category> findById(Long id);
}
