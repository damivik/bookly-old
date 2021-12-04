package com.github.damivik.bookly.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.github.damivik.bookly.entity.Bookshelf;
import com.github.damivik.bookly.entity.User;

public interface BookshelfRepository extends CrudRepository<Bookshelf, Integer> {
	public List<Bookshelf> findByUser(User user);
}
