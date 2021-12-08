package com.github.damivik.bookly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.damivik.bookly.dto.UserRegistration;
import com.github.damivik.bookly.dto.UserUpdate;
import com.github.damivik.bookly.entity.User;
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

	public User register(UserRegistration dto) {
		User user = new User(dto.getEmail(), passwordEncoder.encode(dto.getPassword()));

		return userRepository.save(user);
	}
	
	public void update(UserUpdate dto, int userId) {
		User user = userRepository.findById(userId).get();
		
		if (dto.getEmail() != null) {
			user.setEmail(dto.getEmail());
		}
		if (dto.getPassword() != null) {
			user.setPassword(passwordEncoder.encode(dto.getPassword()));
		}
		
		userRepository.save(user);
	}
	
	public void delete (int userId) {
		userRepository.deleteById(userId);
	}
}
