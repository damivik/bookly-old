package com.github.damivik.bookly.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.github.damivik.bookly.entity.Book;

public interface BookRepository extends CrudRepository<Book, Integer> {
	public List<Book> findByTitleContainingIgnoreCase(String title);
}
