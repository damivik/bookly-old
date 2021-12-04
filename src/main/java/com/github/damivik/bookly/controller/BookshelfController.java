package com.github.damivik.bookly.controller;

import java.net.URI;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.damivik.bookly.dto.BookView;
import com.github.damivik.bookly.dto.BookshelfView;
import com.github.damivik.bookly.dto.BookshelvesView;
import com.github.damivik.bookly.dto.NewBookshelf;
import com.github.damivik.bookly.entity.Bookshelf;
import com.github.damivik.bookly.service.BookshelfService;
import com.github.damivik.bookly.validation.UserExists;

@RestController
public class BookshelfController {
	private BookshelfService bookshelfService;

	public BookshelfController(BookshelfService bookshelfService) {
		this.bookshelfService = bookshelfService;
	}

	@PostMapping("/api/bookshelves")
	public ResponseEntity<String> create(@Valid NewBookshelf dto) {
		Bookshelf bookshelf = bookshelfService.create(dto);
		URI location = URI.create("/bookshelves/" + bookshelf.getId());

		return ResponseEntity.created(location).build();
	}

	@GetMapping(path = "/api/bookshelves/{bookshelfId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public BookshelfView show(@PathVariable int bookshelfId) {
		Bookshelf shelf = bookshelfService.retrieveBookshelf(bookshelfId);

		return new BookshelfView(bookshelfId, shelf.getId(), shelf.getName(), shelf.getBooks().size());
	}

	@DeleteMapping("/api/bookshelves/{bookshelfId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable int bookshelfId) {
		bookshelfService.deleteBookshelf(bookshelfId);
	}

	@GetMapping(path = "/api/bookshelves", produces = MediaType.APPLICATION_JSON_VALUE)
	public BookshelvesView index(@RequestParam @Valid @NotNull @UserExists Integer userId) {
		return new BookshelvesView(bookshelfService.retrieveUserBookshelves(userId));
	}

	@PatchMapping(path = "/api/bookshelves/{bookshelfId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public BookshelfView update(@PathVariable int bookshelfId,
			@RequestParam @Valid @NotBlank @Size(max = 255) String name) {
		Bookshelf bookshelf = bookshelfService.updateBookshelf(bookshelfId, name);

		return new BookshelfView(bookshelfId, bookshelf.getId(), bookshelf.getName(), bookshelf.getBooks().size());
	}

	@GetMapping(path = "/api/bookshelves/{bookshelfId}/books", produces = MediaType.APPLICATION_JSON_VALUE)
	public BookView listBooks(@PathVariable int bookshelfId) {
		return new BookView(bookshelfService.retrieveBooks(bookshelfId));
	}
	
	@PostMapping("/api/bookshelves/{bookshelfId}/books")
	public ResponseEntity<String> addBook(@PathVariable int bookshelfId, @RequestParam int bookId) {
		bookshelfService.addBook(bookshelfId, bookId);
		URI location = URI.create("/bookshelves/" + bookshelfId + "/books/" + bookId);

		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/api/bookshelves/{bookshelfId}/books/{bookId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeBook(@PathVariable int bookshelfId, @PathVariable int bookId) {
		bookshelfService.removeBook(bookshelfId, bookId);
	}
}