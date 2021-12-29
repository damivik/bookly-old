package com.github.damivik.bookly.integrationtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;

@Tag("integration-test")
@SpringBootTest
@AutoConfigureMockMvc
public class BaseTest {

	@Autowired
	protected MockMvc mvc;

	@Autowired
	protected DatabaseHelper databaseHelper;

	@BeforeEach
	public void setUp() {
		databaseHelper.refresh();
	}
}
