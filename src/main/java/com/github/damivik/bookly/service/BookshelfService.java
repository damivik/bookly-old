package com.github.damivik.bookly.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.github.damivik.bookly.entity.Book;
import com.github.damivik.bookly.entity.Bookshelf;
import com.github.damivik.bookly.entity.User;
import com.github.damivik.bookly.exception.BookshelfNotFoundException;
import com.github.damivik.bookly.repository.BookshelfRepository;

@Service
public class BookshelfService {
	private BookshelfRepository bookshelfRepository;

	public BookshelfService(BookshelfRepository bookshelfRepository) {
		this.bookshelfRepository = bookshelfRepository;
	}
	
	public Bookshelf create(User user, String name) { 
		return bookshelfRepository.save(new Bookshelf(user, name));
	}
	
	public Bookshelf retrieveBookshelf(int bookshelfId) throws BookshelfNotFoundException{
		Optional<Bookshelf> optionalBookshelf = bookshelfRepository.findById(bookshelfId);
		
		if (optionalBookshelf.isEmpty()) {
			throw new BookshelfNotFoundException();
		}
		
		return optionalBookshelf.get();
	}

	@PreAuthorize("hasPermission(#bookshelf, 'delete')")
	public void deleteBookshelf(Bookshelf bookshelf) {
		bookshelfRepository.delete(bookshelf);
	}
	
	@PreAuthorize("hasPermission(#bookshelf, 'update')")
	public Bookshelf updateBookshelf(Bookshelf bookshelf, String name) {
		bookshelf.setName(name);
		
		return bookshelfRepository.save(bookshelf);
	}
	
	public List<Bookshelf> retrieveUserBookshelves(User user) {
		return bookshelfRepository.findByUser(user);
	}
	
	@PreAuthorize("hasPermission(#bookshelf, 'addBook')")
	public void addBook(Bookshelf bookshelf, Book book) {
		bookshelf.addBook(book);

		bookshelfRepository.save(bookshelf);
	}
	
	@PreAuthorize("hasPermission(#bookshelf, 'removeBook')")
	public void removeBook(Bookshelf bookshelf, Book book) {
		bookshelf.removeBook(book);

		bookshelfRepository.save(bookshelf);
	}
}