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

import com.modestoneto.pedidos.application.port.in.CreateUserUseCase;
import com.modestoneto.pedidos.application.port.in.DeleteUserUseCase;
import com.modestoneto.pedidos.application.port.in.FindUserByIdUseCase;
import com.modestoneto.pedidos.application.port.in.FindUsersUseCase;
import com.modestoneto.pedidos.application.port.in.UpdateUserUseCase;
import com.modestoneto.pedidos.domain.model.User;

import jakarta.validation.Valid;

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

	@GetMapping
	public ResponseEntity<List<UserResponse>> findAll() {
		List<UserResponse> list = findUsers.findAll().stream().map(this::toResponse).toList();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
		return findUserById.findById(id)
				.map(user -> ResponseEntity.ok(toResponse(user)))
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
		User user = new User(null, request.name(), request.email(), request.phone(), request.password());
		User created = createUser.create(user);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId())
				.toUri();
		return ResponseEntity.created(uri).body(toResponse(created));
	}

	@PutMapping("/{id}")
	public ResponseEntity<UserResponse> update(@PathVariable Long id,
			@Valid @RequestBody UpdateUserRequest request) {
		User data = new User(null, request.name(), request.email(), request.phone(), null);
		return updateUser.update(id, data)
				.map(user -> ResponseEntity.ok(toResponse(user)))
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		if (deleteUser.delete(id)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

	private UserResponse toResponse(User user) {
		return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getPhone());
	}
}
