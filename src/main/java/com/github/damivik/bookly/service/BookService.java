package com.github.damivik.bookly.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.damivik.bookly.entity.Book;
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

	public Book retrieve(int id) {
		return bookRepository.findById(id).get();
	}
}
