package com.github.damivik.bookly.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Bookshelf {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;
	
	@ManyToOne
	private User user;
	
	@Column(nullable = false)
	private String name;
	
	@ManyToMany
	private List<Book> books;
	
	public Bookshelf() {
	}
	
	public Bookshelf(User user, String name) {
		this.user = user;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
	
	public void addBook(Book book) {
		books.add(book);
		book.getBookshelves().add(this);
	}
	
	public void removeBook(Book book) {
		books.remove(book);
		book.getBookshelves().remove(this);
	}
}