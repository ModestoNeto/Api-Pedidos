package com.modestoneto.pedidos.application.port.in;

import com.modestoneto.pedidos.domain.model.Category;

public interface FindCategoryByIdUseCase {

	Category findById(Long id);
}
