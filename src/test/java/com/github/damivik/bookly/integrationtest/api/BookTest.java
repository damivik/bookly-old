package com.github.damivik.bookly.integrationtest.api;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import com.github.damivik.bookly.entity.Book;
import com.github.damivik.bookly.integrationtest.BaseTest;

public class BookTest extends BaseTest {
	
	@Test
	@WithMockUser
	void searchBooksByTitle() throws Exception {
		ArrayList<Book> books = new ArrayList<>();
		books.add(databaseHelper.createBook("Harry Potter and the Philosopher''s Stone", "J. K. Rowling"));
		books.add(databaseHelper.createBook("Harry Potter and the Chamber of Secrets", "J. K. Rowling"));
		books.add(databaseHelper.createBook("To kill a Mockingbird", "Harper Lee"));
		String q = "Harry Potter";
		
		mvc
			.perform(get("/api/books").param("q", q))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.count", is(2)))
			.andExpect(jsonPath("$.books.[0].id", is(books.get(0).getId())))
			.andExpect(jsonPath("$.books.[0].title", is(books.get(0).getTitle())))
			.andExpect(jsonPath("$.books.[0].authors", is(books.get(0).getAuthors())))
			.andExpect(jsonPath("$.books.[1].id", is(books.get(1).getId())))
			.andExpect(jsonPath("$.books.[1].title", is(books.get(1).getTitle())))
			.andExpect(jsonPath("$.books.[1].authors", is(books.get(1).getAuthors())));
	}
	
	@Test
	@WithMockUser
	void retriveBook() throws Exception {
		Book book = databaseHelper.createBook("To kill a Mockingbird", "Harper Lee");
		
		mvc
			.perform(get("/api/books/" + book.getId()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(book.getId())))
			.andExpect(jsonPath("$.title", is(book.getTitle())))
			.andExpect(jsonPath("$.authors", is(book.getAuthors())));
	}
}
