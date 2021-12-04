package com.github.damivik.bookly.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.github.damivik.bookly.dto.UserRegistration;
import com.github.damivik.bookly.entity.User;
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
 		UserRegistration dto = new UserRegistration(email, password);
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

}
