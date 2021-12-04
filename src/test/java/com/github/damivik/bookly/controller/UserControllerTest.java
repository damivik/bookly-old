package com.github.damivik.bookly.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.github.damivik.bookly.dto.UserRegistration;
import com.github.damivik.bookly.entity.User;
import com.github.damivik.bookly.service.UserService;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class UserControllerTest {

	@Mock
	private UserService userService;

	@Test
	void createAccount() {
		String email = "johnDoe@example.com";
		String password = "12345678";
		User expectedUser = new User();
		int userId = 1;
		expectedUser.setId(userId);
		UserRegistration dto = new UserRegistration(email, password);
		Mockito.when(userService.register(dto)).thenReturn(expectedUser);
		UserController controller = new UserController(userService);
		String expectedLocation = "/users/" + userId;
		
		ResponseEntity<String> entity = controller.create(dto);

		Mockito.verify(userService).register(dto);
		assertEquals(expectedLocation, entity.getHeaders().getLocation().getPath()); 
		assertEquals(HttpStatus.CREATED, entity.getStatusCode());
	}

}
