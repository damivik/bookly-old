package com.github.damivik.bookly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.github.damivik.bookly.entity.Book;
import com.github.damivik.bookly.entity.Bookshelf;
import com.github.damivik.bookly.entity.User;
import com.github.damivik.bookly.repository.BookRepository;
import com.github.damivik.bookly.repository.BookshelfRepository;
import com.github.damivik.bookly.repository.UserRepository;

@Service
public class BookshelfService {
	private BookshelfRepository bookshelfRepository;
	private UserRepository userRepository;
	private BookRepository bookRepository;

	public BookshelfService(BookshelfRepository bookshelfRepository) {
		this.bookshelfRepository = bookshelfRepository;
	}

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Autowired
	public void setBookRepository(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}
	
	public Bookshelf create(User user, String name) {
		return bookshelfRepository.save(new Bookshelf(user, name));
	}
	
	public Bookshelf retrieveBookshelf(int bookshelfId){
		return bookshelfRepository.findById(bookshelfId).get();
	}

	@PreAuthorize("hasPermission(#bookshelfId, 'Bookshelf', 'delete')")
	public void deleteBookshelf(int bookshelfId) {
		bookshelfRepository.deleteById(bookshelfId);
	}
	
	public List<Bookshelf> retrieveUserBookshelves(int userId) {
		return bookshelfRepository.findByUser(userRepository.findById(userId).get());
	}
	
	@PreAuthorize("hasPermission(#bookshelfId, 'Bookshelf', 'updateBookshelf')")
	public Bookshelf updateBookshelf(int bookshelfId, String name) {
		Bookshelf shelf = bookshelfRepository.findById(bookshelfId).get();
		shelf.setName(name);
		
		return bookshelfRepository.save(shelf);
	}

	@PreAuthorize("hasPermission(#bookshelfId, 'Bookshelf', 'addBook')")
	public void addBook(int bookshelfId, int bookId) {
		Bookshelf bookshelf = bookshelfRepository.findById(bookshelfId).get();
		Book book = bookRepository.findById(bookId).get();
		bookshelf.addBook(book);

		bookshelfRepository.save(bookshelf);
	}
	
	@PreAuthorize("hasPermission(#bookshelfId, 'Bookshelf', 'removeBook')")
	public void removeBook(int bookshelfId, int bookId) {
		Bookshelf bookshelf = bookshelfRepository.findById(bookshelfId).get();
		Book book = bookRepository.findById(bookId).get();
		bookshelf.removeBook(book);
		bookshelf.getBooks().remove(book);

		bookshelfRepository.save(bookshelf);
	}
	
	public List<Book> retrieveBooks(int bookshelfId) {
		return bookshelfRepository.findById(bookshelfId).get().getBooks();
	}
}