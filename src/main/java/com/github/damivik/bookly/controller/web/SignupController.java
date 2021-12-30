package com.github.damivik.bookly.controller.web;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.github.damivik.bookly.dto.SignupDto;
import com.github.damivik.bookly.service.UserService;

@Controller
public class SignupController {

	private UserService userService;

	public SignupController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/signup")
	public String showForm(SignupDto signupDto) {
		return "signup";
	}

	@PostMapping("/signup")
	public String processForm(@Valid SignupDto signup, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "signup";
		}

		userService.register(signup);

		return "redirect:/login";
	}
}
