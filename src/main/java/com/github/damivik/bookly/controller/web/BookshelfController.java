package com.github.damivik.bookly.controller.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.github.damivik.bookly.entity.User;
import com.github.damivik.bookly.service.BookshelfService;

@Controller
public class BookshelfController {
	private BookshelfService bookshelfService;
	
	public BookshelfController(BookshelfService bookshelfService) {
		this.bookshelfService  = bookshelfService;
	}
	
	@GetMapping("/")
	public String showAllUserBookshelves(Authentication authentication, Model model) {
		User user = (User) authentication.getPrincipal();
		model.addAttribute("bookshelves", bookshelfService.retrieveUserBookshelves(user));
		
		return "home";
	}
	
}
