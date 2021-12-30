package com.github.damivik.bookly.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.github.damivik.bookly.dto.SignupDto;
import com.github.damivik.bookly.dto.UserUpdate;
import com.github.damivik.bookly.entity.User;
import com.github.damivik.bookly.exception.UserNotFoundException;
import com.github.damivik.bookly.repository.UserRepository;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;
	
	@Mock
	private PasswordEncoder passwordEncoder;

	@Test
	void register() {
		String email = "johndoe@example.com";
		String password = "password";
 		SignupDto dto = new SignupDto(email, password);
		User expectedUser = new User();
		Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(expectedUser);
		UserService service = new UserService(userRepository);
		String encodedPassword = "encoded_password";
		Mockito.when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
		service.setPasswordEncoder(passwordEncoder);
		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

		User actualUser = service.register(dto);

		Mockito.verify(userRepository).save(captor.capture());
		assertSame(expectedUser, actualUser);
		assertEquals(email, captor.getValue().getEmail());
		assertEquals(encodedPassword, captor.getValue().getPassword());
	}
	
	@Test
	void retrieve_throwUserNotFoundException_whenUserWithSuppliedIdDoesNotExist() {
		int userId = 1;
		UserService service = new UserService(userRepository);
		
		assertThrows(UserNotFoundException.class, () -> service.retrieve(userId));
	}
	
	@Test
	void retrieve_returnUser_whenUserWithSuppliedIdExist() throws UserNotFoundException {
		int userId = 1;
		User expectedUser = new User();
		Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
		UserService service = new UserService(userRepository);
		
		User actualUser = service.retrieve(userId);
		
		assertSame(expectedUser, actualUser);
	}
	
	@Test
	void update() throws UserNotFoundException {
		String email = "johndoe@example.com";
		String password = "password"; 
		String encodedPassword = "encoded_password";
		UserUpdate dto = new UserUpdate();
		dto.setEmail(email);
		dto.setPassword(password);
		User user = new User();
		UserService userService = new UserService(userRepository);
		Mockito.when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
		userService.setPasswordEncoder(passwordEncoder);
		
		userService.update(user, dto);
		
		Mockito.verify(userRepository).save(user);
		assertEquals(email, user.getEmail());
		assertEquals(encodedPassword, user.getPassword());
	}
	
	@Test
	void delete() throws UserNotFoundException {
		User user = new User();
		UserService userService = new UserService(userRepository);
		
		userService.delete(user);
		
		Mockito.verify(userRepository).delete(user);
	}
}
