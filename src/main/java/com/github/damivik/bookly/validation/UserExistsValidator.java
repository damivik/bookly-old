package com.github.damivik.bookly.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.damivik.bookly.repository.UserRepository;

public class UserExistsValidator implements ConstraintValidator<UserExists, Integer> {
	@Autowired
	private UserRepository userRepository;

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		return this.userRepository.existsById(value);
	}
}
