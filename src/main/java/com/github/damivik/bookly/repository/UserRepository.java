package com.github.damivik.bookly.repository;

import org.springframework.data.repository.CrudRepository;

import com.github.damivik.bookly.entity.User;

public interface UserRepository extends CrudRepository<User, Integer> {
	public boolean existsByEmail(String email);
	
	public User findByEmail(String email);
}