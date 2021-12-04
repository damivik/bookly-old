package com.github.damivik.bookly.dto;

import java.util.ArrayList;
import java.util.List;

import com.github.damivik.bookly.entity.Bookshelf;

public class BookshelvesView {
	private int items;
	private List<BookshelfView> bookshelves;
	
	public BookshelvesView(List<Bookshelf> userBookshelves) {
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

	public class BookshelfView {
		private int id;
		private String name;
		private int bookCount;
		
		public BookshelfView(Bookshelf shelf) {
			this.id = shelf.getId();
			this.name = shelf.getName();
			this.bookCount = shelf.getBooks().size();
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
}
