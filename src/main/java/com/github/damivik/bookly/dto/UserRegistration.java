package com.github.damivik.bookly.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.github.damivik.bookly.validation.UniqueEmail;

public class UserRegistration {
	@NotBlank
	@Size(max = 255)
	@Email
	@UniqueEmail
	private String email;

	@NotBlank
	@Size(min = 8, max = 30)
	private String password;
	
	public UserRegistration(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}