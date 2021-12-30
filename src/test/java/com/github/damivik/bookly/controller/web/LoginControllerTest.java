package com.github.damivik.bookly.controller.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LoginController.class)
class LoginControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@Test
	void loginFormIsDisplayed() throws Exception {
		mvc
			.perform(
					get("/login"))
			.andExpect(status().isOk())
			.andExpect(view().name("login"));
	}

}
