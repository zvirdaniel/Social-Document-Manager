package cz.zvir.social.controllers;

import cz.zvir.social.responses.UserResponse;
import cz.zvir.social.services.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService service;

	@GetMapping
	@ApiOperation("Returns all available users.")
	public List<UserResponse> getAll() {
		return service.getAll().stream().map(UserResponse::new).collect(Collectors.toList());
	}

	@GetMapping("/id/{id}")
	@ApiOperation("Returns single user by user ID.")
	public UserResponse getById(@PathVariable final long id) {
		return new UserResponse(service.get(id));
	}

	@PostMapping
	@ApiOperation("Creates new user.")
	public UserResponse create(@RequestParam final String name) {
		return new UserResponse(service.create(name));
	}

	@PatchMapping("/id/{id}")
	@ApiOperation("Updates user with given user ID.")
	public UserResponse update(@PathVariable final long id, @RequestParam final String name) {
		return new UserResponse(service.update(id, name));
	}

	@DeleteMapping("/id/{id}")
	@ApiOperation("Deletes user with given user ID.")
	public void delete(@PathVariable final long id) {
		service.delete(id);
	}
}
