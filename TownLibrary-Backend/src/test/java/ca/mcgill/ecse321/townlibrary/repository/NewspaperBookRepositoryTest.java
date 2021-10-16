package ca.mcgill.ecse321.townlibrary.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.townlibrary.model.*;

public class NewspaperBookRepositoryTest {
	
	@Autowired
	private NewspaperRepository newspaperRepository;
	@Autowired
	private BookRepository bookRepository;
	
	@AfterEach
	public void clearDatabase() {
		newspaperRepository.deleteAll();
		bookRepository.deleteAll();
	}
	
	@Test
	public void testPersistAndLoadNewspaper() {
		Integer newsId = 3301;
		String name = "Globe&Mail";
		Status status = Status.AVAILABLE;
		Transaction transaction = new Transaction();
		Newspaper newspaper = new Newspaper();
		newspaper.setId(newsId);
		newspaper.setName(name);
		newspaper.setStatus(status);
		newspaper.setTransaction(transaction);
		newspaperRepository.save(newspaper);
		newspaper = null;
		
		newspaper = newspaperRepository.findNewspaperById(newsId);
		assertNotNull(newspaper);
		assertEquals(newsId, newspaper.getId());
		assertEquals(name, newspaper.getName());
		assertEquals(status, newspaper.getStatus());
		assertEquals(transaction, newspaper.getTransaction());
	}
	
	@Test
	public void testPersistAndLoadBook() {
		Integer bookId = 2501;
		String name = "Dune";
		Status status = Status.RESERVED;
		Transaction transaction = new Transaction();
		Book book = new Book();
		book.setId(bookId);
		book.setName(name);
		book.setStatus(status);
		book.setTransaction(transaction);
		bookRepository.save(book);
		book = null;
		
		book = bookRepository.findBookById(bookId);
		assertNotNull(book);
		assertEquals(bookId, book.getId());
		assertEquals(name, book.getName());
		assertEquals(status, book.getStatus());
		assertEquals(transaction, book.getTransaction());
	}

}
