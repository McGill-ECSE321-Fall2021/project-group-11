package ca.mcgill.ecse321.townlibrary.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.townlibrary.model.*;

@SpringBootTest
public class NewspaperBookRepositoryTest {
	
	@Autowired
	private NewspaperRepository newspaperRepository;
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private TransactionRepository transactionRepository;
	
	@AfterEach
	public void clearDatabase() {
		newspaperRepository.deleteAll();
		bookRepository.deleteAll();
		transactionRepository.deleteAll();
	}
	
	@Test
	public void testPersistAndLoadNewspaper() {
		// setup associated transaction object
		Transaction transaction = new Transaction();
		transaction.setId(44);
		transactionRepository.save(transaction);
		
		// write Newspaper
		Integer newsId = 3301;
		String name = "Globe&Mail";
		Status status = Status.AVAILABLE;
		Newspaper newspaper = new Newspaper();
		newspaper.setId(newsId);
		newspaper.setName(name);
		newspaper.setStatus(status);
		newspaper.setTransaction(transaction);
		newspaperRepository.save(newspaper);
		newspaper = null;
		
		// read Newspaper
		newspaper = newspaperRepository.findNewspaperById(newsId);
		assertNotNull(newspaper);
		assertEquals(newsId, newspaper.getId());
		assertEquals(name, newspaper.getName());
		assertEquals(status, newspaper.getStatus());
		assertEquals(44, newspaper.getTransaction().getId());
		
		List<Newspaper> newspaperList = newspaperRepository.findNewspaperByName(name);
		assertNotNull(newspaperList);
		assertEquals(newsId, newspaperList.get(0).getId());
		assertEquals(name, newspaperList.get(0).getName());
		assertEquals(status, newspaperList.get(0).getStatus());
		assertEquals(44, newspaperList.get(0).getTransaction().getId());
	}
	
	@Test
	public void testPersistAndLoadBook() {
		// setup associated transaction object
		Transaction transaction = new Transaction();
		transaction.setId(55);
		transactionRepository.save(transaction);
		
		// write Book
		Integer bookId = 2501;
		String name = "Dune";
		Status status = Status.RESERVED;
		Book book = new Book();
		book.setId(bookId);
		book.setName(name);
		book.setStatus(status);
		book.setTransaction(transaction);
		bookRepository.save(book);
		book = null;
		
		// read Book
		book = bookRepository.findBookById(bookId);
		assertNotNull(book);
		assertEquals(bookId, book.getId());
		assertEquals(name, book.getName());
		assertEquals(status, book.getStatus());
		assertEquals(55, book.getTransaction().getId());
		
		List<Book> bookList = bookRepository.findByNameContaining("Du");
		assertNotNull(bookList);
		assertEquals(bookId, bookList.get(0).getId());
		assertEquals(name, bookList.get(0).getName());
		assertEquals(status, bookList.get(0).getStatus());
		assertEquals(55, bookList.get(0).getTransaction().getId());
	}

}
