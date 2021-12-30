package com.github.damivik.bookly.controller.api;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import com.github.damivik.bookly.dto.SignupDto;
import com.github.damivik.bookly.dto.UserUpdate;
import com.github.damivik.bookly.entity.User;
import com.github.damivik.bookly.exception.UserNotFoundException;
import com.github.damivik.bookly.service.UserService;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class UserControllerTest {

	@Mock
	private UserService userService;

	@Test
	void create() {
		String email = "johnDoe@example.com";
		String password = "12345678";
		User expectedUser = new User();
		int userId = 1;
		expectedUser.setId(userId);
		SignupDto dto = new SignupDto(email, password);
		Mockito.when(userService.register(dto)).thenReturn(expectedUser);
		UserController controller = new UserController(userService);
		String expectedLocation = "/users/" + userId;

		ResponseEntity<String> entity = controller.create(dto);

		Mockito.verify(userService).register(dto);
		assertEquals(expectedLocation, entity.getHeaders().getLocation().getPath());
		assertEquals(HttpStatus.CREATED, entity.getStatusCode());
	}

	@Test
	void update_throwsResponseStatusExceptionWithHttpNotFoundStatus_whenUserServiceThrowsUserNotFoundException()
			throws UserNotFoundException {

		UserUpdate dto = new UserUpdate();
		int userId = 1;
		Mockito.when(userService.retrieve(userId)).thenThrow(UserNotFoundException.class);

		UserController controller = new UserController(userService);

		ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> controller.update(userId, dto));
		assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
	}

	@Test
	void update() throws UserNotFoundException {
		UserUpdate dto = new UserUpdate();
		int userId = 1;
		User user = new User();
		Mockito.when(userService.retrieve(userId)).thenReturn(user);
		UserController controller = new UserController(userService);

		controller.update(userId, dto);

		Mockito.verify(userService).update(user, dto);
	}

	@Test
	void delete_throwsResponseStatusExceptionWithHttpNotFoundStatus_whenUserServiceThrowsUserNotFoundException()
			throws UserNotFoundException {

		int userId = 1;
		Mockito.when(userService.retrieve(userId)).thenThrow(UserNotFoundException.class);

		UserController controller = new UserController(userService);

		ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> controller.delete(userId));
		assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
	}

	@Test
	void delete() throws UserNotFoundException {
		int userId = 1;
		User user = new User();
		Mockito.when(userService.retrieve(userId)).thenReturn(user);
		UserController controller = new UserController(userService);

		controller.delete(userId);

		Mockito.verify(userService).delete(user);
	}

}
