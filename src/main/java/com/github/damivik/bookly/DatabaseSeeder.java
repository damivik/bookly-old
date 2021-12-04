package com.github.damivik.bookly;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.github.damivik.bookly.entity.Book;
import com.github.damivik.bookly.entity.Bookshelf;
import com.github.damivik.bookly.entity.User;
import com.github.damivik.bookly.repository.BookRepository;
import com.github.damivik.bookly.repository.BookshelfRepository;
import com.github.damivik.bookly.repository.UserRepository;

@Component
public class DatabaseSeeder implements ApplicationRunner {
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookshelfRepository bookshelfRepository;

	@Autowired
	private BookRepository bookRepository;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		User user = userRepository.save(new User("damivik@example.com", passwordEncoder.encode("12345678")));

		List<Book> nonfictionBooks = Arrays.asList(
				bookRepository.save(new Book("Sapiens: A Brief History of Humankind", "Yuval Noah Harari")),
				bookRepository.save(
						new Book("The Power of Habit: Why We Do What We Do in Life and Business", "Charles Duhigg")),
				bookRepository.save(
						new Book("Quiet: The Power of Introverts in a World That Can't Stop Talking", "Susan Cain")),
				bookRepository.save(new Book("Outliers: The Story of Success", "Malcolm Gladwell")),
				bookRepository.save(new Book(
						"The Innovators: How a Group of Hackers, Geniuses and Geeks Created the Digital Revolution",
						"Walter Isaacson")),
				bookRepository.save(new Book("Born a Crime: Stories From a South African Childhood", "Trevor Noah")),
				bookRepository.save(new Book("The Body: A Guide for Occupants", "Bill Bryson")),
				bookRepository.save(new Book("Night", "Elie Wiesel, Marion Wiesel(Translator)")),
				bookRepository.save(new Book("Thinking, Fast and Slow", "Daniel Kahneman")),
				bookRepository.save(new Book("Cosmos", "Carl Sagan")));

		Bookshelf nonfiction = bookshelfRepository.save(new Bookshelf(user, "Non-fiction"));
		nonfiction.setBooks(nonfictionBooks);
		bookshelfRepository.save(nonfiction);
		
		List<Book> mysteryThrillersBooks = Arrays.asList(
				bookRepository.save(new Book("Gone Girl", "Gillian Flynn")),
				bookRepository.save(new Book("The Girl on the Train", "Paula Hawkins")),
				bookRepository.save(new Book("The Da Vinci Code", "Dan Brown")),
				bookRepository.save(new Book("The Girl with the Dragon Tattoo", " Stieg Larsson")),
				bookRepository.save(new Book("The Silent Patient", " Alex Michaelides")),
				bookRepository.save(new Book("And Then There Were None", "Agatha Christie")),
				bookRepository.save(new Book("The Woman in the Window", "A.J. Finn")),
				bookRepository.save(new Book("Murder on the Orient Express", "Agatha Christie")),
				bookRepository.save(new Book("In the Woods", "Tana French")),
				bookRepository.save(new Book("The Cuckoo's Calling", "Robert Galbraith")));
		
		Bookshelf mysteryThrillers = bookshelfRepository.save(new Bookshelf(user, "Mystery/Thrillers"));
		mysteryThrillers.setBooks(mysteryThrillersBooks);
		bookshelfRepository.save(mysteryThrillers);
		
		List<Book> classicsBooks = Arrays.asList(
				bookRepository.save(new Book("Pride and Prejudice", "Jane Austen")),
				bookRepository.save(new Book("The Great Gatsby", "F. Scott Fitzgerald")),
				bookRepository.save(new Book("To Kill a Mockingbird", "Harper Lee")),
				bookRepository.save(new Book("Jane Eyre", "Charlotte Brontë")),
				bookRepository.save(new Book("1984", "George Orwell")),
				bookRepository.save(new Book("Wuthering Heights", "Emily Brontë")),
				bookRepository.save(new Book("The Catcher in the Rye", "J.D. Salinger ")),
				bookRepository.save(new Book("The Picture of Dorian Gray", "Oscar Wilde")),
				bookRepository.save(new Book("Of Mice and Men", "John Steinbeck")),
				bookRepository.save(new Book("Great Expectations", "Charles Dickens")));
		
		Bookshelf classics = bookshelfRepository.save(new Bookshelf(user, "Classics"));
		classics.setBooks(classicsBooks);
		bookshelfRepository.save(classics);
		
		List<Book> fantasyBooks = Arrays.asList(
				bookRepository.save(new Book("Harry Potter and the Philosopher's Stone", "J. K. Rowling")),
				bookRepository.save(new Book("Harry Potter and the Chamber of Secrets", "J. K. Rowling")),
				bookRepository.save(new Book("Harry Potter and the Prisoner of Azkaban", "J. K. Rowling")),
				bookRepository.save(new Book("Harry Potter and the Goblet of Fire", "J. K. Rowling")),
				bookRepository.save(new Book("Harry Potter and the Order of the Phoenix", "J. K. Rowling")),
				bookRepository.save(new Book("Harry Potter and the Half-Blood Prince", "J. K. Rowling")),
				bookRepository.save(new Book("Harry Potter and the Deathly Hallows", "J. K. Rowling")),
				bookRepository.save(new Book("A Game of Thrones", "George R.R. Martin")),
				bookRepository.save(new Book("The Name of the Wind", "Patrick Rothfuss")),
				bookRepository.save(new Book("The Hunger Games", "Suzanne Collins")));
		
		Bookshelf fantasy = bookshelfRepository.save(new Bookshelf(user, "Fantasy"));
		fantasy.setBooks(fantasyBooks);
		bookshelfRepository.save(fantasy);
	}
}