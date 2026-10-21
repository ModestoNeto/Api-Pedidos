package com.modestoneto.pedidos.adapter.in.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.modestoneto.pedidos.application.port.in.FindProductByIdUseCase;
import com.modestoneto.pedidos.application.port.in.FindProductsUseCase;
import com.modestoneto.pedidos.domain.exception.ResourceNotFoundException;
import com.modestoneto.pedidos.domain.model.Category;
import com.modestoneto.pedidos.domain.model.Product;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockitoBean
	private FindProductsUseCase findProducts;

	@MockitoBean
	private FindProductByIdUseCase findProductById;

	private Product notebook() {
		Product product = new Product(3L, "Notebook de Exemplo", "Descrição", 1250.0, "");
		product.getCategories().add(new Category(1L, "Eletrônicos"));
		product.getCategories().add(new Category(3L, "Computadores"));
		return product;
	}

	@Test
	void buscaProdutoComSuasCategorias() throws Exception {
		when(findProductById.findById(3L)).thenReturn(notebook());

		mvc.perform(get("/products/3"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Notebook de Exemplo"))
				.andExpect(jsonPath("$.price").value(1250.0))
				.andExpect(jsonPath("$.categories.length()").value(2))
				.andExpect(jsonPath("$.categories[0].name").value("Eletrônicos"));
	}

	@Test
	void listaOsProdutos() throws Exception {
		when(findProducts.findAll()).thenReturn(List.of(notebook()));

		mvc.perform(get("/products"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].id").value(3));
	}

	@Test
	void idInexistenteRetorna404NoFormatoPadrao() throws Exception {
		when(findProductById.findById(999L)).thenThrow(new ResourceNotFoundException("Produto não encontrado: id 999"));

		mvc.perform(get("/products/999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Produto não encontrado: id 999"))
				.andExpect(jsonPath("$.path").value("/products/999"));
	}
}
