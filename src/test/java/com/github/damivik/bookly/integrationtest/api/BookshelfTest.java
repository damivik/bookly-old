package com.github.damivik.bookly.integrationtest.api;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.github.damivik.bookly.entity.Book;
import com.github.damivik.bookly.entity.Bookshelf;
import com.github.damivik.bookly.entity.User;
import com.github.damivik.bookly.integrationtest.BaseTest;

class BookshelfTest extends BaseTest {
	@Test
	void create() throws Exception {
		User user = databaseHelper.createUser();
		String bookshelfName = "Mystery/Thrillers";

		mvc
			.perform(
					post("/api/bookshelves")
					.param("name", bookshelfName).with(httpBasic(user.getUsername(), "password")))
			.andExpect(status().isCreated())
			.andExpect(header().exists("Location"));
	}

	@Test
	void read() throws Exception {
		User user = databaseHelper.createUser();
		Bookshelf bookshelf = databaseHelper.createBookshelf(user);

		mvc
			.perform(
					get("/api/bookshelves/" + bookshelf.getId())
					.with(httpBasic(user.getUsername(), "password")))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(bookshelf.getId())))
			.andExpect(jsonPath("$.name", is(bookshelf.getName())))
			.andExpect(jsonPath("$.bookCount", is(bookshelf.getBooks().size())));
	}

	@Test
	void read_returnHttpNotFoundStatusCode_whenBookshelfDoesNotExist() throws Exception {
		User user = databaseHelper.createUser();
		int nonExistentBookshelfid = 0;

		mvc
			.perform(
					get("/api/bookshelves/" + nonExistentBookshelfid)
					.with(httpBasic(user.getUsername(), "password")))
			.andExpect(status().isNotFound());
	}

	@Test
	void delete() throws Exception {
		User user = databaseHelper.createUser();
		Bookshelf bookshelf = databaseHelper.createBookshelf(user);

		mvc
			.perform(MockMvcRequestBuilders
					.delete("/api/bookshelves/" + bookshelf.getId())
					.with(httpBasic(user.getUsername(), "password")))
			.andExpect(status().isNoContent());
	}
	
	@Test
	void delete_whenAuthenticatedUserNotOwnerOfBookshelf_returnsForbiddenStatus() throws Exception {
		User shelfOwner = databaseHelper.createUser();
		User notShelfOwner = databaseHelper.createUser("not_shelf_owner@example.com");
		Bookshelf bookshelf = databaseHelper.createBookshelf(shelfOwner);

		mvc
			.perform(MockMvcRequestBuilders
					.delete("/api/bookshelves/" + bookshelf.getId())
					.with(httpBasic(notShelfOwner.getUsername(), "password")))
			.andExpect(status().isForbidden());
	}

	
	@Test
	void update() throws Exception {
		User user = databaseHelper.createUser();
		Bookshelf bookshelf = databaseHelper.createBookshelf(user);
		String newName = "Non-Fiction";

		mvc
			.perform(
					patch("/api/bookshelves/" + bookshelf.getId())
					.param("name", newName)
					.with(httpBasic(user.getUsername(), "password")))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(bookshelf.getId())))
			.andExpect(jsonPath("$.name", is(newName)))
			.andExpect(jsonPath("$.bookCount", is(bookshelf.getBooks().size())));
	}
	
	@Test
	void update_whenAuthenticatedUserNotOwnerOfBookshelf_returnForbiddenStatus() throws Exception {
		User shelfOwner = databaseHelper.createUser();
		User notShelfOwner = databaseHelper.createUser("not_shelf_owner@example.com");
		Bookshelf bookshelf = databaseHelper.createBookshelf(shelfOwner);
		String newName = "Non-Fiction";

		mvc
			.perform(
					patch("/api/bookshelves/" + bookshelf.getId())
					.param("name", newName)
					.with(httpBasic(notShelfOwner.getUsername(), "password")))
			.andExpect(status().isForbidden());
	}

	@Test
	void retrieveUserBookshelves() throws Exception {
		User user = databaseHelper.createUser();
		List<Bookshelf> bookshelves = databaseHelper.createBookshelves(user);

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
	void retrieveBooks() throws Exception {
		User user = databaseHelper.createUser();
		Bookshelf bookshelf = databaseHelper.createBookshelf(user);
		
		mvc
			.perform(
				get("/api/bookshelves/" + bookshelf.getId() + "/books")
				.with(httpBasic(user.getUsername(), "password")))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.count", is(bookshelf.getBooks().size())))
			.andExpect(jsonPath("$.books[0].id", is(bookshelf.getBooks().get(0).getId())))
			.andExpect(jsonPath("$.books[0].authors", is(bookshelf.getBooks().get(0).getAuthors())))
			.andExpect(jsonPath("$.books[0].title", is(bookshelf.getBooks().get(0).getTitle())))
			.andExpect(jsonPath("$.books[1].id", is(bookshelf.getBooks().get(1).getId())))
			.andExpect(jsonPath("$.books[1].authors", is(bookshelf.getBooks().get(1).getAuthors())))
			.andExpect(jsonPath("$.books[1].title", is(bookshelf.getBooks().get(1).getTitle())));
	}
	
	@Test
	void addBook() throws Exception {
		User user = databaseHelper.createUser();
		Bookshelf bookshelf = databaseHelper.createBookshelf(user);
		Book book = databaseHelper.createBook();

		mvc
			.perform(
					post("/api/bookshelves/" + bookshelf.getId() + "/books")
					.param("bookId", book.getId().toString())
					.with(httpBasic(user.getUsername(), "password")))
			.andExpect(status().isNoContent());
	}
	
	@Test
	void addBook_whenAuthenticatedUserNotOwnerOfBookshelf_returnForbiddenStatus() throws Exception {
		User shelfOwner = databaseHelper.createUser();
		User notShelfOwner = databaseHelper.createUser("not_shelf_owner@example.com");
		Bookshelf bookshelf = databaseHelper.createBookshelf(shelfOwner);
		Book book = databaseHelper.createBook();

		mvc
			.perform(
					post("/api/bookshelves/" + bookshelf.getId() + "/books")
					.param("bookId", book.getId().toString())
					.with(httpBasic(notShelfOwner.getUsername(), "password")))
			.andExpect(status().isForbidden());

	}

	@Test
	void removeBook() throws Exception {
		User user = databaseHelper.createUser();
		Bookshelf bookshelf = databaseHelper.createBookshelf(user);

		mvc
			.perform(
					MockMvcRequestBuilders
					.delete("/api/bookshelves/" + bookshelf.getId() + "/books/" + bookshelf.getBooks().get(0).getId())
					.with(httpBasic(user.getUsername(), "password")))
			.andExpect(status().isNoContent());
	}
	
	@Test
	void removeBook_whenAuthenticatedUserNotOwnerOfBookshelf_returnForbiddenStatus() throws Exception {
		User shelfOwner = databaseHelper.createUser();
		User notShelfOwner = databaseHelper.createUser("not_shelf_owner@example.com");
		Bookshelf bookshelf = databaseHelper.createBookshelf(shelfOwner);

		mvc.perform(
				MockMvcRequestBuilders
				.delete("/api/bookshelves/" + bookshelf.getId() + "/books/" + bookshelf.getBooks().get(0).getId())
				.with(httpBasic(notShelfOwner.getUsername(), "password")))
		.andExpect(status().isForbidden());

	}
}
