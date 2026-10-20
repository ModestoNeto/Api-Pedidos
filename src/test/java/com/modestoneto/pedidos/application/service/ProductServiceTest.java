package com.modestoneto.pedidos.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.modestoneto.pedidos.application.port.out.ProductRepositoryPort;
import com.modestoneto.pedidos.domain.exception.ResourceNotFoundException;
import com.modestoneto.pedidos.domain.model.Product;

class ProductServiceTest {

	private ProductRepositoryPort repository;
	private ProductService service;

	@BeforeEach
	void setUp() {
		repository = mock(ProductRepositoryPort.class);
		service = new ProductService(repository);
	}

	@Test
	void findByIdDevolveOProdutoQuandoExiste() {
		Product product = new Product(1L, "Produto de Exemplo", "Descrição", 10.0, "");
		when(repository.findById(1L)).thenReturn(Optional.of(product));

		assertEquals(product, service.findById(1L));
	}

	@Test
	void findByIdLancaExcecaoQuandoNaoExiste() {
		when(repository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> service.findById(999L));
	}
}
