package com.modestoneto.pedidos.adapter.in.web;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.modestoneto.pedidos.adapter.in.web.exception.StandardError;
import com.modestoneto.pedidos.application.port.in.CreateUserUseCase;
import com.modestoneto.pedidos.application.port.in.DeleteUserUseCase;
import com.modestoneto.pedidos.application.port.in.FindUserByIdUseCase;
import com.modestoneto.pedidos.application.port.in.FindUsersUseCase;
import com.modestoneto.pedidos.application.port.in.UpdateUserUseCase;
import com.modestoneto.pedidos.domain.model.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Usuários", description = "Cadastro, consulta, atualização e remoção de usuários")
@RestController
@RequestMapping("/users")
public class UserController {

	private final FindUsersUseCase findUsers;
	private final FindUserByIdUseCase findUserById;
	private final CreateUserUseCase createUser;
	private final UpdateUserUseCase updateUser;
	private final DeleteUserUseCase deleteUser;

	public UserController(FindUsersUseCase findUsers, FindUserByIdUseCase findUserById,
			CreateUserUseCase createUser, UpdateUserUseCase updateUser, DeleteUserUseCase deleteUser) {
		this.findUsers = findUsers;
		this.findUserById = findUserById;
		this.createUser = createUser;
		this.updateUser = updateUser;
		this.deleteUser = deleteUser;
	}

	@Operation(summary = "Lista todos os usuários")
	@GetMapping
	public ResponseEntity<List<UserResponse>> findAll() {
		List<UserResponse> list = findUsers.findAll().stream().map(this::toResponse).toList();
		return ResponseEntity.ok(list);
	}

	@Operation(summary = "Busca um usuário por id")
	@ApiResponse(responseCode = "400", description = "Id com formato inválido",
			content = @Content(schema = @Schema(implementation = StandardError.class)))
	@ApiResponse(responseCode = "404", description = "Usuário não encontrado",
			content = @Content(schema = @Schema(implementation = StandardError.class)))
	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
		return ResponseEntity.ok(toResponse(findUserById.findById(id)));
	}

	@Operation(summary = "Cria um usuário", description = "Devolve 201 com o header Location do novo usuário. A senha nunca é devolvida.")
	@ApiResponse(responseCode = "400", description = "Dados inválidos (nome, e-mail ou senha) ou corpo ilegível",
			content = @Content(schema = @Schema(implementation = StandardError.class)))
	@PostMapping
	public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
		User user = new User(null, request.name(), request.email(), request.phone(), request.password());
		User created = createUser.create(user);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId())
				.toUri();
		return ResponseEntity.created(uri).body(toResponse(created));
	}

	@Operation(summary = "Atualiza nome, e-mail e telefone de um usuário", description = "A senha não é alterada por este endpoint.")
	@ApiResponse(responseCode = "400", description = "Dados inválidos ou corpo ilegível",
			content = @Content(schema = @Schema(implementation = StandardError.class)))
	@ApiResponse(responseCode = "404", description = "Usuário não encontrado",
			content = @Content(schema = @Schema(implementation = StandardError.class)))
	@PutMapping("/{id}")
	public ResponseEntity<UserResponse> update(@PathVariable Long id,
			@Valid @RequestBody UpdateUserRequest request) {
		User data = new User(null, request.name(), request.email(), request.phone(), null);
		return ResponseEntity.ok(toResponse(updateUser.update(id, data)));
	}

	@Operation(summary = "Remove um usuário")
	@ApiResponse(responseCode = "204", description = "Usuário removido")
	@ApiResponse(responseCode = "404", description = "Usuário não encontrado",
			content = @Content(schema = @Schema(implementation = StandardError.class)))
	@ApiResponse(responseCode = "409", description = "O usuário possui pedidos e não pode ser removido",
			content = @Content(schema = @Schema(implementation = StandardError.class)))
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		deleteUser.delete(id);
		return ResponseEntity.noContent().build();
	}

	private UserResponse toResponse(User user) {
		return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getPhone());
	}
}
