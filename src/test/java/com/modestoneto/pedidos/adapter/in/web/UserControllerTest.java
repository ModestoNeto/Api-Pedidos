package com.modestoneto.pedidos.adapter.in.web;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.modestoneto.pedidos.application.port.in.CreateUserUseCase;
import com.modestoneto.pedidos.application.port.in.DeleteUserUseCase;
import com.modestoneto.pedidos.application.port.in.FindUserByIdUseCase;
import com.modestoneto.pedidos.application.port.in.FindUsersUseCase;
import com.modestoneto.pedidos.application.port.in.UpdateUserUseCase;
import com.modestoneto.pedidos.domain.exception.ConflictException;
import com.modestoneto.pedidos.domain.exception.ResourceNotFoundException;
import com.modestoneto.pedidos.domain.model.User;

@WebMvcTest(UserController.class)
class UserControllerTest {

	private static final String VALID_CREATE = """
			{"name":"Carla Exemplo","email":"carla@exemplo.com","phone":"(00) 00000-0004","password":"senha-ficticia"}""";

	@Autowired
	private MockMvc mvc;

	@MockitoBean
	private FindUsersUseCase findUsers;

	@MockitoBean
	private FindUserByIdUseCase findUserById;

	@MockitoBean
	private CreateUserUseCase createUser;

	@MockitoBean
	private UpdateUserUseCase updateUser;

	@MockitoBean
	private DeleteUserUseCase deleteUser;

	@Test
	void listaOsUsuariosSemExporASenha() throws Exception {
		when(findUsers.findAll()).thenReturn(List.of(
				new User(1L, "Maria Exemplo", "maria@exemplo.com", "1", "senha-ficticia")));

		mvc.perform(get("/users"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].name").value("Maria Exemplo"))
				.andExpect(jsonPath("$[0].password").doesNotExist());
	}

	@Test
	void buscaUsuarioPorId() throws Exception {
		when(findUserById.findById(1L))
				.thenReturn(new User(1L, "Maria Exemplo", "maria@exemplo.com", "1", "senha-ficticia"));

		mvc.perform(get("/users/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("maria@exemplo.com"))
				.andExpect(jsonPath("$.password").doesNotExist());
	}

	@Test
	void idInexistenteRetorna404NoFormatoPadrao() throws Exception {
		when(findUserById.findById(999L)).thenThrow(new ResourceNotFoundException("Usuário não encontrado: id 999"));

		mvc.perform(get("/users/999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.error").value("Recurso não encontrado"))
				.andExpect(jsonPath("$.message").value("Usuário não encontrado: id 999"))
				.andExpect(jsonPath("$.path").value("/users/999"))
				.andExpect(jsonPath("$.timestamp").exists());
	}

	@Test
	void idComTipoErradoRetorna400() throws Exception {
		mvc.perform(get("/users/abc"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("Requisição inválida"))
				.andExpect(jsonPath("$.path").value("/users/abc"));
	}

	@Test
	void criaUsuarioRetorna201ComLocation() throws Exception {
		when(createUser.create(any(User.class)))
				.thenReturn(new User(4L, "Carla Exemplo", "carla@exemplo.com", "(00) 00000-0004", "senha-ficticia"));

		mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(VALID_CREATE))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "http://localhost/users/4"))
				.andExpect(jsonPath("$.id").value(4))
				.andExpect(jsonPath("$.password").doesNotExist());
	}

	@Test
	void criarComNomeVazioRetorna400ESemChamarOCasoDeUso() throws Exception {
		String body = """
				{"name":"","email":"carla@exemplo.com","phone":"1","password":"senha-ficticia"}""";

		mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("Erro de validação"))
				.andExpect(jsonPath("$.message").value(containsString("name")));

		verify(createUser, never()).create(any());
	}

	@Test
	void criarComEmailInvalidoRetorna400() throws Exception {
		String body = """
				{"name":"Carla","email":"isso-nao-e-email","phone":"1","password":"senha-ficticia"}""";

		mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(containsString("email")));
	}

	@Test
	void criarComSenhaVaziaRetorna400() throws Exception {
		String body = """
				{"name":"Carla","email":"carla@exemplo.com","phone":"1","password":""}""";

		mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(containsString("password")));
	}

	@Test
	void corpoIlegivelRetorna400() throws Exception {
		mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content("{\"name\": \"Carla\", "))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("Requisição inválida"))
				.andExpect(jsonPath("$.message").value("Corpo da requisição ilegível"));
	}

	@Test
	void atualizaUsuario() throws Exception {
		when(updateUser.update(eq(1L), any(User.class)))
				.thenReturn(new User(1L, "Novo Nome", "novo@exemplo.com", "9", "senha-ficticia"));
		String body = """
				{"name":"Novo Nome","email":"novo@exemplo.com","phone":"9"}""";

		mvc.perform(put("/users/1").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Novo Nome"))
				.andExpect(jsonPath("$.password").doesNotExist());
	}

	@Test
	void atualizarIdInexistenteRetorna404() throws Exception {
		when(updateUser.update(eq(999L), any(User.class)))
				.thenThrow(new ResourceNotFoundException("Usuário não encontrado: id 999"));
		String body = """
				{"name":"Novo Nome","email":"novo@exemplo.com","phone":"9"}""";

		mvc.perform(put("/users/999").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.path").value("/users/999"));
	}

	@Test
	void atualizarComDadosInvalidosRetorna400() throws Exception {
		String body = """
				{"name":"","email":"xxx","phone":"9"}""";

		mvc.perform(put("/users/1").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("Erro de validação"));

		verify(updateUser, never()).update(any(), any());
	}

	@Test
	void removeUsuarioRetorna204() throws Exception {
		mvc.perform(delete("/users/3"))
				.andExpect(status().isNoContent());

		verify(deleteUser).delete(3L);
	}

	@Test
	void removerIdInexistenteRetorna404() throws Exception {
		doThrow(new ResourceNotFoundException("Usuário não encontrado: id 999")).when(deleteUser).delete(999L);

		mvc.perform(delete("/users/999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Usuário não encontrado: id 999"));
	}

	@Test
	void removerUsuarioComPedidosRetorna409() throws Exception {
		doThrow(new ConflictException("Não é possível remover o usuário: ele possui pedidos"))
				.when(deleteUser).delete(1L);

		mvc.perform(delete("/users/1"))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.status").value(409))
				.andExpect(jsonPath("$.error").value("Conflito"))
				.andExpect(jsonPath("$.path").value("/users/1"));
	}
}
