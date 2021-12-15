package com.github.damivik.bookly.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.github.damivik.bookly.entity.Book;
import com.github.damivik.bookly.exception.BookNotFoundException;
import com.github.damivik.bookly.repository.BookRepository;

@Service
public class BookService {
	private BookRepository bookRepository;

	public BookService(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	public List<Book> search(String title) {
		return bookRepository.findByTitleContainingIgnoreCase(title);
	}

	public Book retrieve(int id) throws BookNotFoundException {
		Optional<Book> optionalBook = bookRepository.findById(id);
		
		if (optionalBook.isEmpty()) {
			throw new BookNotFoundException();
		}
		
		return optionalBook.get();
	}
}
