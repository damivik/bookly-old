package com.github.damivik.bookly.controller.api;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.github.damivik.bookly.dto.SignupDto;
import com.github.damivik.bookly.dto.UserUpdate;
import com.github.damivik.bookly.entity.User;
import com.github.damivik.bookly.exception.UserNotFoundException;
import com.github.damivik.bookly.service.UserService;

@RestController
public class UserController {
	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/api/users")
	public ResponseEntity<String> create(@Valid SignupDto dto) {
		User user = userService.register(dto);
		URI location = URI.create("/users/" + user.getId());
		
		return ResponseEntity.created(location).body("");
	}
	
	@PatchMapping("/api/users/{userId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update(@PathVariable int userId, @Valid UserUpdate dto) {
		User user;
		
		try {
			user = userService.retrieve(userId);
		} catch (UserNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		userService.update(user, dto);
	}
	
	@DeleteMapping("/api/users/{userId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable int userId) {
		User user;
		
		try {
			user = userService.retrieve(userId);
		} catch (UserNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		userService.delete(user);
	}
}
