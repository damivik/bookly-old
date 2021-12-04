package com.github.damivik.bookly.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.github.damivik.bookly.validation.UserExists;

public class NewBookshelf {
	@NotNull
	@UserExists
	private int userId;
	
	@NotBlank
	@Size(max = 255)
	private String name;
	
	public NewBookshelf(int userId, String name) {
		this.userId = userId;
		this.name = name;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
