package com.github.damivik.bookly.integrationtest.api;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import com.github.damivik.bookly.entity.User;
import com.github.damivik.bookly.integrationtest.BaseTest;

public class UserTest extends BaseTest {
	
	@Test
	void create_whenRequestParametersAreValid_returnCreatedStatus() throws Exception {
		String email = "dami@example.com";
		String password = "password";
		
		mvc
			.perform(
					post("/api/users")
					.param("email", email)
					.param("password", password))
			.andExpect(
					status()
					.isCreated());
	}
	
	@Test
	void update_returnNoContentStatus() throws Exception {
		User user = databaseHelper.createUser();
		String newEmail = "damivik@example.com";
		String newPassword = "new_password";
		
		mvc
			.perform(
					patch("/api/users/" + user.getId())
					.param("email", newEmail)
					.param("password", newPassword)
					.with(httpBasic(user.getEmail(), "password")))
			.andExpect(
					status()
					.isNoContent());
	}
	
	@Test
	void update_whenUserWithSuppliedUserIdDoesNotExist_returnNotFoundStatus() throws Exception {
		int nonExistingUserId = 0;
		User user = databaseHelper.createUser();
		String newEmail = "damivik@example.com";
		String newPassword = "new_password";
		
		mvc
			.perform(
					patch("/api/users/" + nonExistingUserId)
					.param("email", newEmail)
					.param("password", newPassword)
					.with(httpBasic(user.getEmail(), "password")))
			.andExpect(
					status()
					.isNotFound());
	}
	
	@Test
	void update_whenAuthenticatedUserNotOwnerOfAccount_returnsForbiddenStatus() throws Exception {
		User accountOwner = databaseHelper.createUser();
		User notAccountOwner = databaseHelper.createUser("not_account_owner@example.com");
		String newEmail = "new_email@example.com";
		String newPassword = "new_password";
		
		mvc
			.perform(
					patch("/api/users/" + accountOwner.getId())
					.param("email", newEmail)
					.param("password", newPassword)
					.with(httpBasic(notAccountOwner.getEmail(), "password")))
			.andExpect(
					status()
					.isForbidden());
	}
	
	@Test
	void delete_returnNoContentStatus() throws Exception {
		User user = databaseHelper.createUser();
		
		mvc
			.perform(
					delete("/api/users/" + user.getId())
					.with(httpBasic(user.getEmail(), "password")))
			.andExpect(
					status()
					.isNoContent());
	}
	
	@Test
	void delete_whenAuthenticatedUserNotOwnerOfAccount_returnsForbiddenStatus() throws Exception {
		User accountOwner = databaseHelper.createUser();
		User notAccountOwner = databaseHelper.createUser("not_account_owner@example.com");
		
		mvc
			.perform(
					delete("/api/users/" + accountOwner.getId())
					.with(httpBasic(notAccountOwner.getEmail(), "password")))
			.andExpect(
					status()
					.isForbidden());
	}
	
	@Test
	void delete_whenUserWithSuppliedUserIdDoesNotExist_returnNotFoundStatus() throws Exception {
		int nonExistingUserId = 0;
		User user = databaseHelper.createUser();
		
		mvc
			.perform(
					delete("/api/users/" + nonExistingUserId)
					.with(httpBasic(user.getEmail(), "password")))
			.andExpect(
					status()
					.isNotFound());
	}
}
