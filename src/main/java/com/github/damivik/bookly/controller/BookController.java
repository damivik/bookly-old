package com.github.damivik.bookly.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.damivik.bookly.dto.BookView;
import com.github.damivik.bookly.entity.Book;
import com.github.damivik.bookly.service.BookService;

@RestController
public class BookController {
	private BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@GetMapping(path = "/api/books", produces = MediaType.APPLICATION_JSON_VALUE)
	public BookView search(@RequestParam @Valid @NotBlank String q) {
		return new BookView(bookService.search(q));
	}

	@GetMapping(path = "/api/books/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Book show(@PathVariable int id){
		return bookService.retrieve(id);
	}

}
