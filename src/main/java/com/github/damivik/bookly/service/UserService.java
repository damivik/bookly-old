package com.github.damivik.bookly.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.damivik.bookly.dto.SignupDto;
import com.github.damivik.bookly.dto.UserUpdate;
import com.github.damivik.bookly.entity.User;
import com.github.damivik.bookly.exception.UserNotFoundException;
import com.github.damivik.bookly.repository.UserRepository;

@Service
public class UserService {
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Autowired
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public User register(SignupDto dto) {
		User user = new User(dto.getEmail(), passwordEncoder.encode(dto.getPassword()));

		return userRepository.save(user);
	}
	
	public User retrieve(int userId) throws UserNotFoundException {
		Optional<User> optionalUser = userRepository.findById(userId);
		if (optionalUser.isEmpty())
			throw new UserNotFoundException();
		
		return optionalUser.get();
	}

	@PreAuthorize("hasPermission(#user, 'update')")
	public void update(User user, UserUpdate dto) {
		if (dto.getEmail() != null) {
			user.setEmail(dto.getEmail());
		}
		if (dto.getPassword() != null) {
			user.setPassword(passwordEncoder.encode(dto.getPassword()));
		}

		userRepository.save(user);
	}

	@PreAuthorize("hasPermission(#user, 'delete')")
	public void delete(User user) {
		userRepository.delete(user);
	}
}
