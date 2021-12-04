package com.github.damivik.bookly.dto;

import java.util.List;

import com.github.damivik.bookly.entity.Book;

public class BookView {
	private int count;
	private List<Book> books;

	public BookView(List<Book> books) {
		this.count = books.size();
		this.books = books;
	}

	public int getCount() {
		return count;
	}

	public List<Book> getBooks() {
		return books;
	}
}
