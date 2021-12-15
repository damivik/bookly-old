package com.github.damivik.bookly.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.github.damivik.bookly.entity.Book;
import com.github.damivik.bookly.entity.Bookshelf;
import com.github.damivik.bookly.entity.User;
import com.github.damivik.bookly.exception.BookshelfNotFoundException;
import com.github.damivik.bookly.repository.BookshelfRepository;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class BookshelfServiceTest {

	@Mock
	private BookshelfRepository bookshelfRepository;
	
	@Test
	void create() {
		User user = new User();
		String name = "Fiction";
		Bookshelf expectedBookshelf = new Bookshelf();
		Mockito.when(bookshelfRepository.save(Mockito.any(Bookshelf.class))).thenReturn(expectedBookshelf);
		BookshelfService service = new BookshelfService(bookshelfRepository);
		ArgumentCaptor<Bookshelf> bookshelfCaptor = ArgumentCaptor.forClass(Bookshelf.class);
		
		Bookshelf actualBookshelf = service.create(user, name);
		
		Mockito.verify(bookshelfRepository).save(bookshelfCaptor.capture());
		assertSame(expectedBookshelf, actualBookshelf);
		assertSame(user, bookshelfCaptor.getValue().getUser());
		assertSame(name, bookshelfCaptor.getValue().getName());
	}

	@Test
	void retrieveBookshelf_throwBookshelfNotFoundException_whenBookshelfWithSuppliedIdDoesNotExist() {
		int bookshelfId = 1;
		BookshelfService service = new BookshelfService(bookshelfRepository);
		
		assertThrows(BookshelfNotFoundException.class, () -> service.retrieveBookshelf(bookshelfId));
	}
	
	@Test
	void retrieveBookshelf_returnBookshelf_whenBookshelfWithSuppliedIdExist() throws Exception {
		int bookshelfId = 1;
		Bookshelf expectedBookshelf = new Bookshelf();
		Mockito.when(bookshelfRepository.findById(bookshelfId)).thenReturn(Optional.of(expectedBookshelf));
		BookshelfService service = new BookshelfService(bookshelfRepository);
		
		Bookshelf actualBookshelf =  service.retrieveBookshelf(bookshelfId);
		
		assertSame(expectedBookshelf, actualBookshelf);
	}
	
	@Test
	void deleteBookshelf() {
		Bookshelf bookshelf = new Bookshelf();
		BookshelfService service = new BookshelfService(bookshelfRepository);
		
		service.deleteBookshelf(bookshelf);
		
		Mockito.verify(bookshelfRepository).delete(bookshelf);
	}
	
	@Test
	void retrieveUserBookshelves() {
		List<Bookshelf> expectedBookshelves = new ArrayList<>();
		User user = new User();
		Mockito.when(bookshelfRepository.findByUser(user)).thenReturn(expectedBookshelves);
		BookshelfService service = new BookshelfService(bookshelfRepository);
		
		List<Bookshelf> actualBookshelves = service.retrieveUserBookshelves(user);
		
		assertSame(expectedBookshelves, actualBookshelves);
	}
	
	@Test
	void addBook() {
		Bookshelf bookshelf = new Bookshelf();
		bookshelf.setBooks(new ArrayList<>());
		Book book = new Book();
		Mockito.when(bookshelfRepository.save(bookshelf)).thenReturn(bookshelf);
		BookshelfService service = new BookshelfService(bookshelfRepository);
		
		service.addBook(bookshelf, book);
		
		assertTrue(bookshelf.getBooks().contains(book));
	}
	
	@Test
	void removeBook() {
		Bookshelf bookshelf = new Bookshelf();
		Book book = new Book();
		List<Book> books = new ArrayList<>();
		books.add(book);
		bookshelf.setBooks(books);
	
		Mockito.when(bookshelfRepository.save(bookshelf)).thenReturn(bookshelf);
		BookshelfService service = new BookshelfService(bookshelfRepository);
		
		service.removeBook(bookshelf, book);
		
		assertFalse(bookshelf.getBooks().contains(book));
	}
}
