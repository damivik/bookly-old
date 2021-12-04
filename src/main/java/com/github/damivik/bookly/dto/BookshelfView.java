package com.github.damivik.bookly.dto;

public class BookshelfView {
	private int id;
	private int userId;
	private String name;
	private int booksCount;


	public BookshelfView(int id, int userId, String name, int booksCount) {
		this.id = id;
		this.userId = userId;
		this.name = name;
		this.booksCount = booksCount;
	}

	public int getId() {
		return id;
	}

	public int getUserId() {
		return userId;
	}
	
	public String getName() {
		return name;
	}

	public int getBooksCount() {
		return booksCount;
	}
}