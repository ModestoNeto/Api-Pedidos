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

import com.modestoneto.pedidos.application.port.in.FindCategoriesUseCase;
import com.modestoneto.pedidos.application.port.in.FindCategoryByIdUseCase;
import com.modestoneto.pedidos.domain.exception.ResourceNotFoundException;
import com.modestoneto.pedidos.domain.model.Category;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockitoBean
	private FindCategoriesUseCase findCategories;

	@MockitoBean
	private FindCategoryByIdUseCase findCategoryById;

	@Test
	void listaAsCategorias() throws Exception {
		when(findCategories.findAll()).thenReturn(List.of(new Category(1L, "Livros"), new Category(2L, "Eletrônicos")));

		mvc.perform(get("/categories"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[1].name").value("Eletrônicos"));
	}

	@Test
	void buscaCategoriaPorId() throws Exception {
		when(findCategoryById.findById(1L)).thenReturn(new Category(1L, "Livros"));

		mvc.perform(get("/categories/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("Livros"));
	}

	@Test
	void idInexistenteRetorna404NoFormatoPadrao() throws Exception {
		when(findCategoryById.findById(999L))
				.thenThrow(new ResourceNotFoundException("Categoria não encontrada: id 999"));

		mvc.perform(get("/categories/999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Categoria não encontrada: id 999"))
				.andExpect(jsonPath("$.path").value("/categories/999"));
	}
}
