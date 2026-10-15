package com.modestoneto.pedidos.application.port.in;

import java.util.List;

import com.modestoneto.pedidos.domain.model.Category;

public interface FindCategoriesUseCase {

	List<Category> findAll();
}
