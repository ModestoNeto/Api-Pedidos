package com.modestoneto.pedidos.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.modestoneto.pedidos.application.port.out.CategoryRepositoryPort;
import com.modestoneto.pedidos.domain.exception.ResourceNotFoundException;
import com.modestoneto.pedidos.domain.model.Category;

class CategoryServiceTest {

	private CategoryRepositoryPort repository;
	private CategoryService service;

	@BeforeEach
	void setUp() {
		repository = mock(CategoryRepositoryPort.class);
		service = new CategoryService(repository);
	}

	@Test
	void findByIdDevolveACategoriaQuandoExiste() {
		Category category = new Category(1L, "Livros");
		when(repository.findById(1L)).thenReturn(Optional.of(category));

		assertEquals(category, service.findById(1L));
	}

	@Test
	void findByIdLancaExcecaoQuandoNaoExiste() {
		when(repository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> service.findById(999L));
	}
}
