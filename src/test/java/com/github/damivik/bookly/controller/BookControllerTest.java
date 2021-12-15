package com.github.damivik.bookly.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.github.damivik.bookly.dto.BookListView;
import com.github.damivik.bookly.entity.Book;
import com.github.damivik.bookly.exception.BookNotFoundException;
import com.github.damivik.bookly.service.BookService;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class BookControllerTest {

	@Mock
	private BookService bookService;

	@Test
	void search() {
		String title = "Harry Potter";
		int bookCount = 7;
		List<Book> searchResult = Arrays.asList(new Book("Harry Potter and the Philosopher's Stone", "J. K. Rowling"),
				new Book("Harry Potter and the Chamber of Secrets", "J. K. Rowling"),
				new Book("Harry Potter and the Prisoner of Azkaban", "J. K. Rowling"),
				new Book("Harry Potter and the Goblet of Fire", "J. K. Rowling"),
				new Book("Harry Potter and the Order of the Phoenix", "J. K. Rowling"),
				new Book("Harry Potter and the Half-Blood Prince", "J. K. Rowling"),
				new Book("Harry Potter and the Deathly Hallows", "J. K. Rowling"));
		Mockito.when(bookService.search(title)).thenReturn(searchResult);
		BookController bookController = new BookController(bookService);

		BookListView dto = bookController.search(title);

		assertSame(searchResult, dto.getBooks());
		assertEquals(bookCount, dto.getCount());
	}

	@Test
	void read_throwsResponseStatusExceptionWithHttpNotFoundStatus_whenBookServiceThrowsABookNotFoundException()
			throws BookNotFoundException {
		int bookId = 1;
		BookController bookController = new BookController(bookService);
		Mockito.when(bookService.retrieve(bookId)).thenThrow(BookNotFoundException.class);

		ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> bookController.read(bookId));
		assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
	}
	
	@Test
	void read() throws BookNotFoundException {
		int bookId = 1;
		Book expectedBook = new Book();
		Mockito.when(bookService.retrieve(bookId)).thenReturn(expectedBook);
		BookController bookController = new BookController(bookService);

		Book actualBook = bookController.read(bookId);
		
		assertSame(expectedBook, actualBook);
	}
}
