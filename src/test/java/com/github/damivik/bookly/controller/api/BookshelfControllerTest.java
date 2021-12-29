package com.github.damivik.bookly.controller.api;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

import com.github.damivik.bookly.dto.BookListView;
import com.github.damivik.bookly.dto.BookshelfListView;
import com.github.damivik.bookly.dto.BookshelfView;
import com.github.damivik.bookly.entity.Book;
import com.github.damivik.bookly.entity.Bookshelf;
import com.github.damivik.bookly.entity.User;
import com.github.damivik.bookly.exception.BookshelfNotFoundException;
import com.github.damivik.bookly.service.BookService;
import com.github.damivik.bookly.service.BookshelfService;
import com.github.damivik.bookly.service.UserService;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class BookshelfControllerTest {

	@Mock
	private BookshelfService bookshelfService;
	
	@Mock
	private UserService userService;
	
	@Mock
	private BookService bookService;

	@Mock
	Authentication authentication;

	@Test
	void create() {
		String name = "Fiction";
		User user = new User();
		Bookshelf bookshelf = new Bookshelf();
		int bookshelfId = 1;
		bookshelf.setId(bookshelfId);
		Mockito.when(authentication.getPrincipal()).thenReturn(user);
		Mockito.when(bookshelfService.create(user, name)).thenReturn(bookshelf);
		BookshelfController controller = new BookshelfController(bookshelfService);
		String expectedLocation = "/bookshelves/" + bookshelfId;

		ResponseEntity<String> responseEntity = controller.create(authentication, name);

		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals(expectedLocation, responseEntity.getHeaders().getLocation().getPath());
	}

	@Test
	void read_throwsResponseStatusExceptionWithNotFoundCode_whenBookshelfServiceThrowsBookshelfNotFoundException()
			throws BookshelfNotFoundException {
		int bookshelfId = 1;
		Mockito.when(bookshelfService.retrieveBookshelf(bookshelfId)).thenThrow(BookshelfNotFoundException.class);
		BookshelfController controller = new BookshelfController(bookshelfService);

		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> controller.read(bookshelfId));
		assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
	}

	@Test
	void read() throws BookshelfNotFoundException {
		int bookshelfId = 1;
		String name = "Fiction";
		List<Book> books = Arrays.asList(new Book(), new Book());
		Bookshelf bookshelf = new Bookshelf();
		bookshelf.setId(bookshelfId);
		bookshelf.setName(name);
		bookshelf.setBooks(books);
		Mockito.when(bookshelfService.retrieveBookshelf(bookshelfId)).thenReturn(bookshelf);
		BookshelfController controller = new BookshelfController(bookshelfService);

		BookshelfView dto = controller.read(bookshelfId);
		
		assertEquals(bookshelfId, dto.getId());
		assertSame(name, dto.getName());
		assertSame(2, dto.getBookCount());
	}

	@Test
	void delete() throws Exception {
		int bookshelfId = 1;
		Bookshelf bookshelf = new Bookshelf();
		Mockito.when(bookshelfService.retrieveBookshelf(bookshelfId)).thenReturn(bookshelf);
		BookshelfController controller = new BookshelfController(bookshelfService);
		
		controller.delete(bookshelfId);
		
		Mockito.verify(bookshelfService).deleteBookshelf(bookshelf);
	}
	
	@Test
	void update() throws BookshelfNotFoundException {
		int bookshelfId = 1;
		String name = "Fiction";
		List<Book> books = Arrays.asList(new Book(), new Book());
		Bookshelf bookshelf = new Bookshelf();
		bookshelf.setId(bookshelfId);
		bookshelf.setName(name);
		bookshelf.setBooks(books);
		Mockito.when(bookshelfService.retrieveBookshelf(bookshelfId)).thenReturn(bookshelf);
		Mockito.when(bookshelfService.updateBookshelf(bookshelf, name)).thenReturn(bookshelf);
		BookshelfController controller = new BookshelfController(bookshelfService);

		BookshelfView dto = controller.update(bookshelfId, name);
		
		assertEquals(bookshelfId, dto.getId());
		assertSame(name, dto.getName());
		assertSame(2, dto.getBookCount());
	}

	@Test
	void retrieveUserBookshelves() throws Exception {
		int userId = 1;
		User user = new User();
		List<Bookshelf> bookshelves = getBookshelves();
		Mockito.when(bookshelfService.retrieveUserBookshelves(user)).thenReturn(bookshelves);
		Mockito.when(userService.retrieve(userId)).thenReturn(user);
		BookshelfController controller = new BookshelfController(bookshelfService);
		controller.setUserService(userService);
		
		BookshelfListView dto = controller.retrieveUserBookshelves(userId);
		
		assertEquals(bookshelves.size(), dto.getItems());
		assertEquals(bookshelves.get(0).getId(), dto.getBookshelves().get(0).getId());
		assertSame(bookshelves.get(0).getName(), dto.getBookshelves().get(0).getName());
		assertSame(bookshelves.get(0).getBooks().size(), dto.getBookshelves().get(0).getBookCount());
		assertEquals(bookshelves.get(1).getId(), dto.getBookshelves().get(1).getId());
		assertSame(bookshelves.get(1).getName(), dto.getBookshelves().get(1).getName());
		assertSame(bookshelves.get(1).getBooks().size(), dto.getBookshelves().get(1).getBookCount());
	}
	
	@Test
	void retrieveBooks() throws Exception {
		int bookshelfId = 1;
		Bookshelf bookshelf = new Bookshelf();
		bookshelf.setBooks(getBooks());
		Mockito.when(bookshelfService.retrieveBookshelf(bookshelfId)).thenReturn(bookshelf);
		BookshelfController controller = new BookshelfController(bookshelfService);
		
		BookListView dto = controller.retrieveBooks(bookshelfId);
		
		assertSame(bookshelf.getBooks(), dto.getBooks());
		assertEquals(bookshelf.getBooks().size(), dto.getCount());
	}
	
	@Test
	void addBook() throws Exception {
		int bookshelfId = 1;
		Bookshelf bookshelf = new Bookshelf();
		int bookId = 2;
		Book book = new Book();
		Mockito.when(bookshelfService.retrieveBookshelf(bookshelfId)).thenReturn(bookshelf);
		Mockito.when(bookService.retrieve(bookId)).thenReturn(book);
		BookshelfController controller = new BookshelfController(bookshelfService);
		controller.setBookService(bookService);
		
		controller.addBook(bookshelfId, bookId);
		
		Mockito.verify(bookshelfService).addBook(bookshelf, book);
	}
	
	@Test
	void removeBook() throws Exception {
		int bookshelfId = 1;
		Bookshelf bookshelf = new Bookshelf();
		int bookId = 2;
		Book book = new Book();
		Mockito.when(bookshelfService.retrieveBookshelf(bookshelfId)).thenReturn(bookshelf);
		Mockito.when(bookService.retrieve(bookId)).thenReturn(book);
		BookshelfController controller = new BookshelfController(bookshelfService);
		controller.setBookService(bookService);
		
		controller.removeBook(bookshelfId, bookId);
		
		Mockito.verify(bookshelfService).removeBook(bookshelf, book);
	}
	
	private List<Bookshelf> getBookshelves() { 
		Bookshelf bookshelf1 = new Bookshelf();
		bookshelf1.setId(1);
		bookshelf1.setName("Fiction");
		bookshelf1.setBooks(Arrays.asList(new Book(), new Book()));
		
		Bookshelf bookshelf2 = new Bookshelf();
		bookshelf2.setId(2);
		bookshelf2.setName("Non Fiction");
		bookshelf2.setBooks(Arrays.asList(new Book(), new Book(), new Book()));
		
		return Arrays.asList(bookshelf1, bookshelf2);
	}
	
	private List<Book> getBooks() {
		Book book1 = new Book("John Green", "The Fault In Our Stars");
		Book book2 = new Book("Markus Zusak", "The Book Thief");
		
		return Arrays.asList(book1, book2);
	}
}
