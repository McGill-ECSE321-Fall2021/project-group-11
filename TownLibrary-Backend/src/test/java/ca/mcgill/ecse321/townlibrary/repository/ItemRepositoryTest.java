package ca.mcgill.ecse321.townlibrary.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.townlibrary.model.*;

@SpringBootTest
public class ItemRepositoryTest {
	
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private TransactionRepository transactionRepository;
	
	@AfterEach
	public void clearDatabase() {
		itemRepository.deleteAll();
		transactionRepository.deleteAll();
	}
	
	@Test
	public void testPersistAndLoadItem() {
		Integer id1 = 3301;
		String name1 = "Gazette";
		Status status1 = Status.AVAILABLE;
		Transaction transaction1 = new Transaction();
		transaction1.setId(11);
		transactionRepository.save(transaction1);
		Newspaper gazette = new Newspaper();
		gazette.setId(id1);
		gazette.setName(name1);
		gazette.setStatus(status1);
		gazette.setTransaction(transaction1);
		itemRepository.save(gazette);
		
		Integer id2 = 2501;
		String name2 = "Dune";
		Status status2 = Status.RESERVED;
		Transaction transaction2 = new Transaction();
		transaction1.setId(22);
		transactionRepository.save(transaction2);
		Book dune = new Book();
		dune.setId(id2);
		dune.setName(name2);
		dune.setStatus(status2);
		dune.setTransaction(transaction2);
		itemRepository.save(dune);
		
		Integer id3 = 1991;
		String name3 = "Flowers for Algernon";
		Status status3 = Status.RESERVED;
		Transaction transaction3 = new Transaction();
		transaction1.setId(33);
		transactionRepository.save(transaction3);
		Book algernon = new Book();
		algernon.setId(id3);
		algernon.setName(name3);
		algernon.setStatus(status3);
		algernon.setTransaction(transaction3);
		itemRepository.save(algernon);
		
		Item item;
		item = itemRepository.findItemById(id1);
		assertNotNull(item);
		assertEquals(id1, item.getId());
		
		item = itemRepository.findItemById(id2);
		assertNotNull(item);
		assertEquals(id2, item.getId());
		
		item = itemRepository.findItemByTransaction(transaction3);
		assertNotNull(item);
		assertEquals(33, item.getTransaction().getId());
		
	}

}
