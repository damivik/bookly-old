package com.github.damivik.bookly.dto;

import java.util.ArrayList;
import java.util.List;

import com.github.damivik.bookly.entity.Bookshelf;

public class BookshelfListView {
	private int items;
	private List<BookshelfView> bookshelves;
	
	public BookshelfListView(List<Bookshelf> userBookshelves) {
		this.items = userBookshelves.size();
		bookshelves = new ArrayList<>();
		for(Bookshelf shelf : userBookshelves) {
			bookshelves.add(new BookshelfView(shelf));
		}
	}
	
	public int getItems() {
		return items;
	}

	public List<BookshelfView> getBookshelves() {
		return bookshelves;
	}
}
