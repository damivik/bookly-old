package com.github.damivik.bookly.dto;

import com.github.damivik.bookly.entity.Bookshelf;

public class BookshelfView {
	private int id;
	private String name;
	private int bookCount;


	public BookshelfView(Bookshelf bookshelf) {
		this.id = bookshelf.getId();
		this.name = bookshelf.getName();
		this.bookCount = bookshelf.getBooks().size();
	}

	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public int getBookCount() {
		return bookCount;
	}
}