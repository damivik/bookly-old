package com.github.damivik.bookly.controller.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.github.damivik.bookly.repository.UserRepository;
import com.github.damivik.bookly.service.UserService;

@WebMvcTest(SignupController.class)
class SignupControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private UserRepository userRepository;
	
	@Test
	void signupFormIsDisplayed() throws Exception {
		mvc
			.perform(
					get("/signup"))
			.andExpect(status().isOk())
			.andExpect(view().name("signup"));
	}

	@Test
	void newUserCanSignup() throws Exception {
		mvc
			.perform(
					post("/signup")
					.param("email", "dami@mail.com")
					.param("password", "password")
					.with(csrf()))
			.andExpect(redirectedUrl("/login"));
	}
}
