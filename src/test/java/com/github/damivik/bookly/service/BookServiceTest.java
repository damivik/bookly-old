package com.github.damivik.bookly.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.github.damivik.bookly.entity.Book;
import com.github.damivik.bookly.exception.BookNotFoundException;
import com.github.damivik.bookly.repository.BookRepository;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class BookServiceTest {

	@Mock
	private BookRepository bookRepository;

	@Test
	void search() {
		String title = "1984";
		List<Book> expectedBooks = new ArrayList<>();
		Mockito.when(bookRepository.findByTitleContainingIgnoreCase(title)).thenReturn(expectedBooks);
		BookService bookService = new BookService(bookRepository);

		List<Book> actualBooks = bookService.search(title);

		assertEquals(expectedBooks, actualBooks);
	}

	@Test
	void retrieve_throwBookNotFoundException_whenBookWithSuppliedIdDoesNotExist() {
		int bookId = 1;
		BookService bookService = new BookService(bookRepository);

		assertThrows(BookNotFoundException.class,
				() -> bookService.retrieve(bookId));
	}

	@Test
	void retrieve_returmBookwhenBookWithSuppliedIdExist() throws Exception {
		int bookId = 1;
		Book expectedBook = new Book();
		Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(expectedBook));
		BookService bookService = new BookService(bookRepository);
		
		Book actualBook = bookService.retrieve(bookId);
		
		assertSame(expectedBook, actualBook);
	}

}
