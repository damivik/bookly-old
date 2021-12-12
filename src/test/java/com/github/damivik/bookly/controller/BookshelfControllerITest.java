package com.github.damivik.bookly.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.github.damivik.bookly.Database;
import com.github.damivik.bookly.entity.Book;
import com.github.damivik.bookly.entity.Bookshelf;
import com.github.damivik.bookly.entity.User;

@Tag("it")
@SpringBootTest
@AutoConfigureMockMvc
class BookshelfControllerITest {
	@Autowired
	private Database database;

	@Autowired
	private MockMvc mvc;

	@BeforeEach
	public void setUp() {
		database.refresh();
	}

	@Test
	void create() throws Exception {
		User user = database.createUser();
		String bookshelfName = "Mystery/Thrillers";

		mvc
			.perform(
					post("/api/bookshelves")
					.param("name", bookshelfName).with(httpBasic(user.getUsername(), "password")))
			.andExpect(status().isCreated())
			.andExpect(header().exists("Location"));
	}

	@Test
	void retrieve() throws Exception {
		User user = database.createUser();
		Bookshelf bookshelf = database.createBookshelf(user);

		mvc
			.perform(
					get("/api/bookshelves/" + bookshelf.getId())
					.with(httpBasic(user.getUsername(), "password")))
			.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(bookshelf.getId())))
			.andExpect(jsonPath("$.name", is(bookshelf.getName())))
			.andExpect(jsonPath("$.booksCount", is(bookshelf.getBooks().size())));
	}

	@Test
	void delete() throws Exception {
		User user = database.createUser();
		Bookshelf bookshelf = database.createBookshelf(user);

		mvc
			.perform(MockMvcRequestBuilders
					.delete("/api/bookshelves/" + bookshelf.getId())
					.with(httpBasic(user.getUsername(), "password")))
			.andExpect(status().isNoContent());
	}
	
	@Test
	void delete_whenAuthenticatedUserNotOwnerOfBookshelf_returnsForbiddenStatus() throws Exception {
		User shelfOwner = database.createUser();
		User notShelfOwner = database.createUser("not_shelf_owner@example.com");
		Bookshelf bookshelf = database.createBookshelf(shelfOwner);

		mvc
			.perform(MockMvcRequestBuilders
					.delete("/api/bookshelves/" + bookshelf.getId())
					.with(httpBasic(notShelfOwner.getUsername(), "password")))
			.andExpect(status().isForbidden());
	}


	@Test
	void index() throws Exception {
		User user = database.createUser();
		List<Bookshelf> bookshelves = database.createBookshelves(user);

		mvc
			.perform(
					get("/api/bookshelves")
					.param("userId", user.getId().toString())
					.with(httpBasic(user.getUsername(), "password"))).andExpect(status().isOk())
			.andExpect(jsonPath("$.bookshelves[0].id", is(bookshelves.get(0).getId())))
			.andExpect(jsonPath("$.bookshelves[0].name", is(bookshelves.get(0).getName())))
			.andExpect(jsonPath("$.bookshelves[0].bookCount", is(bookshelves.get(0).getBooks().size())))
			.andExpect(jsonPath("$.bookshelves[1].id", is(bookshelves.get(1).getId())))
			.andExpect(jsonPath("$.bookshelves[1].name", is(bookshelves.get(1).getName())))
			.andExpect(jsonPath("$.bookshelves[1].bookCount", is(bookshelves.get(1).getBooks().size())))
			.andExpect(jsonPath("$.items", is(bookshelves.size())));
	}

	@Test
	void update() throws Exception {
		User user = database.createUser();
		Bookshelf bookshelf = database.createBookshelf(user);
		String newName = "Non-Fiction";

		mvc
			.perform(
					patch("/api/bookshelves/" + bookshelf.getId())
					.param("name", newName)
					.with(httpBasic(user.getUsername(), "password")))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(bookshelf.getId())))
			.andExpect(jsonPath("$.name", is(newName)))
			.andExpect(jsonPath("$.booksCount", is(bookshelf.getBooks().size())));
	}
	
	@Test
	void update_whenAuthenticatedUserNotOwnerOfBookshelf_returnForbiddenStatus() throws Exception {
		User shelfOwner = database.createUser();
		User notShelfOwner = database.createUser("not_shelf_owner@example.com");
		Bookshelf bookshelf = database.createBookshelf(shelfOwner);
		String newName = "Non-Fiction";

		mvc
			.perform(
					patch("/api/bookshelves/" + bookshelf.getId())
					.param("name", newName)
					.with(httpBasic(notShelfOwner.getUsername(), "password")))
			.andExpect(status().isForbidden());
	}
	
	@Test
	void addBook() throws Exception {
		User user = database.createUser();
		Bookshelf bookshelf = database.createBookshelf(user);
		Book book = database.createBook();

		mvc
			.perform(
					post("/api/bookshelves/" + bookshelf.getId() + "/books")
					.param("bookId", book.getId().toString())
					.with(httpBasic(user.getUsername(), "password")))
			.andExpect(status().isCreated());
	}
	
	@Test
	void addBook_whenAuthenticatedUserNotOwnerOfBookshelf_returnForbiddenStatus() throws Exception {
		User shelfOwner = database.createUser();
		User notShelfOwner = database.createUser("not_shelf_owner@example.com");
		Bookshelf bookshelf = database.createBookshelf(shelfOwner);
		Book book = database.createBook();

		mvc
			.perform(
					post("/api/bookshelves/" + bookshelf.getId() + "/books")
					.param("bookId", book.getId().toString())
					.with(httpBasic(notShelfOwner.getUsername(), "password")))
			.andExpect(status().isForbidden());

	}

	@Test
	void removeBook() throws Exception {
		User user = database.createUser();
		Bookshelf bookshelf = database.createBookshelf(user);

		mvc
			.perform(
					MockMvcRequestBuilders
					.delete("/api/bookshelves/" + bookshelf.getId() + "/books/" + bookshelf.getBooks().get(0).getId())
					.with(httpBasic(user.getUsername(), "password")))
			.andExpect(status().isNoContent());
	}
	
	@Test
	void removeBook_whenAuthenticatedUserNotOwnerOfBookshelf_returnForbiddenStatus() throws Exception {
		User shelfOwner = database.createUser();
		User notShelfOwner = database.createUser("not_shelf_owner@example.com");
		Bookshelf bookshelf = database.createBookshelf(shelfOwner);

		mvc.perform(
				MockMvcRequestBuilders
				.delete("/api/bookshelves/" + bookshelf.getId() + "/books/" + bookshelf.getBooks().get(0).getId())
				.with(httpBasic(notShelfOwner.getUsername(), "password")))
		.andExpect(status().isForbidden());

	}
}
