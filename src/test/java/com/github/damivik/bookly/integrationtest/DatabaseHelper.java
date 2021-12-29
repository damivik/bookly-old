package com.github.damivik.bookly.integrationtest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.github.damivik.bookly.entity.Book;
import com.github.damivik.bookly.entity.Bookshelf;
import com.github.damivik.bookly.entity.User;
import com.github.damivik.bookly.repository.BookRepository;
import com.github.damivik.bookly.repository.BookshelfRepository;
import com.github.damivik.bookly.repository.UserRepository;

@Component
public class DatabaseHelper {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	BookshelfRepository bookshelfRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public User createUser() {
		return userRepository.save(new User("dami@example.com", passwordEncoder.encode("password")));
	}
	
	public User createUser(String email) {
		return userRepository.save(new User(email, passwordEncoder.encode("password")));
	}

	public Book createBook(String title, String authors) {
		return bookRepository.save(new Book(title, authors));
	}
	
	public Book createBook() {
		return bookRepository.save(new Book("To kill a Mockingbird", "Harper Lee"));
	}
	
	protected List<Book> createBooks () {
		Book book1 = bookRepository.save(new Book("Harry Potter and the Philosopher''s Stone", "J. K. Rowling"));
		Book book2 = bookRepository.save(new Book("Harry Potter and the Chamber of Secrets", "J. K. Rowling"));
		
		ArrayList<Book> books = new ArrayList<>();
		books.add(book1);
		books.add(book2);
		
		return books;
	}
	
	public Bookshelf createBookshelf(User user) {
		Bookshelf bookshelf = bookshelfRepository.save(new Bookshelf(user, "Fiction"));
		List<Book> books = createBooks();
		bookshelf.setBooks(books);
		bookshelfRepository.save(bookshelf);
		
		return bookshelf;
	}

	public List<Bookshelf> createBookshelves(User user) {
		List<Book> fictionBooks = Arrays.asList(
			createBook("John Green", "The Fault In Our Stars"), 
			createBook("Markus Zusak", "The Book Thief"));
			
		List<Book> nonfictionBooks = Arrays.asList(
			createBook("Yuval Noah Harari", "Sapiens: A Brief History of Humankind"), 
			createBook("Bill Bryson", "A Short History of Nearly Everything Paperback"),
			createBook("Daniel Kahneman", "Thinking, Fast and Slow"));
		
		Bookshelf bookshelf1 = bookshelfRepository.save(new Bookshelf(user, "Fiction"));
		bookshelf1.setBooks(fictionBooks);
		bookshelf1 = bookshelfRepository.save(bookshelf1);
		
		Bookshelf bookshelf2 = bookshelfRepository.save(new Bookshelf(user, "Non-fiction"));
		bookshelf2.setBooks(nonfictionBooks);
		bookshelf2 = bookshelfRepository.save(bookshelf2);
		
		return Arrays.asList(bookshelf1, bookshelf2);
	}
	
	public void refresh() {
		bookshelfRepository.deleteAll();
		userRepository.deleteAll();
		bookRepository.deleteAll();
	}
}
