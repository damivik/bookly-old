package com.github.damivik.bookly.controller;

import java.net.URI;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.github.damivik.bookly.dto.BookListView;
import com.github.damivik.bookly.dto.BookshelfView;
import com.github.damivik.bookly.dto.BookshelfListView;
import com.github.damivik.bookly.entity.Book;
import com.github.damivik.bookly.entity.Bookshelf;
import com.github.damivik.bookly.entity.User;
import com.github.damivik.bookly.exception.BookNotFoundException;
import com.github.damivik.bookly.exception.BookshelfNotFoundException;
import com.github.damivik.bookly.exception.UserNotFoundException;
import com.github.damivik.bookly.service.BookService;
import com.github.damivik.bookly.service.BookshelfService;
import com.github.damivik.bookly.service.UserService;
import com.github.damivik.bookly.validation.UserExists;

@RestController
public class BookshelfController {
	private BookshelfService bookshelfService;
	private UserService userService;
	private BookService bookService;

	public BookshelfController(BookshelfService bookshelfService) {
		this.bookshelfService = bookshelfService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setBookService(BookService bookService) {
		this.bookService = bookService;
	}

	@PostMapping("/api/bookshelves")
	public ResponseEntity<String> create(Authentication authentication, @RequestParam @Valid @NotBlank String name) {
		Bookshelf bookshelf = bookshelfService.create((User) authentication.getPrincipal(), name);
		URI location = URI.create("/bookshelves/" + bookshelf.getId());

		return ResponseEntity.created(location).build();
	}

	@GetMapping(path = "/api/bookshelves/{bookshelfId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public BookshelfView read(@PathVariable int bookshelfId) {
		Bookshelf bookshelf;
		try {
			bookshelf = bookshelfService.retrieveBookshelf(bookshelfId);
		} catch (BookshelfNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		return new BookshelfView(bookshelf);
	}

	@DeleteMapping("/api/bookshelves/{bookshelfId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable int bookshelfId) {
		Bookshelf shelf;

		try {
			shelf = bookshelfService.retrieveBookshelf(bookshelfId);
		} catch (BookshelfNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		bookshelfService.deleteBookshelf(shelf);
	}

	@PatchMapping(path = "/api/bookshelves/{bookshelfId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public BookshelfView update(@PathVariable int bookshelfId,
			@RequestParam @Valid @NotBlank @Size(max = 255) String name) {
		Bookshelf bookshelf;

		try {
			bookshelf = bookshelfService.retrieveBookshelf(bookshelfId);
		} catch (BookshelfNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		bookshelf = bookshelfService.updateBookshelf(bookshelf, name);
		return new BookshelfView(bookshelf);
	}

	@GetMapping(path = "/api/bookshelves", produces = MediaType.APPLICATION_JSON_VALUE)
	public BookshelfListView retrieveUserBookshelves(@RequestParam @Valid @NotNull @UserExists Integer userId)
			throws UserNotFoundException {
		User user = userService.retrieve(userId);

		return new BookshelfListView(bookshelfService.retrieveUserBookshelves(user));
	}

	@GetMapping(path = "/api/bookshelves/{bookshelfId}/books", produces = MediaType.APPLICATION_JSON_VALUE)
	public BookListView retrieveBooks(@PathVariable int bookshelfId) {
		Bookshelf bookshelf;
		try {
			bookshelf = bookshelfService.retrieveBookshelf(bookshelfId);
		} catch (BookshelfNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return new BookListView(bookshelf.getBooks());
	}

	@PostMapping("/api/bookshelves/{bookshelfId}/books")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void addBook(@PathVariable int bookshelfId, @RequestParam int bookId)
			throws BookNotFoundException {
		Bookshelf bookshelf;

		try {
			bookshelf = bookshelfService.retrieveBookshelf(bookshelfId);
		} catch (BookshelfNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		Book book = bookService.retrieve(bookId);
		bookshelfService.addBook(bookshelf, book);
	}

	@DeleteMapping("/api/bookshelves/{bookshelfId}/books/{bookId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeBook(@PathVariable int bookshelfId, @PathVariable int bookId) throws BookNotFoundException {
		Bookshelf bookshelf;

		try {
			bookshelf = bookshelfService.retrieveBookshelf(bookshelfId);
		} catch (BookshelfNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		Book book = bookService.retrieve(bookId);

		bookshelfService.removeBook(bookshelf, book);
	}
}