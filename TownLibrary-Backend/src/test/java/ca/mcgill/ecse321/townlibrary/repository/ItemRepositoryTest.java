package ca.mcgill.ecse321.townlibrary.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.townlibrary.model.*;

@SpringBootTest
public class ItemRepositoryTest {
	
	@Autowired
	private ItemRepository itemRepository;
	
	@AfterEach
	public void clearDatabase() {
		itemRepository.deleteAll();
	}
	
	@Test
	public void testPersistAndLoadItem() {
		/*
		Newspaper gazette = new Newspaper();
		gazette.setId(3301);
		gazette.setName("Gazette");
		gazette.setStatus(Status.AVAILABLE);
		itemRepository.save(gazette);
		
		Book dune = new Book();
		dune.setId(2501);
		dune.setName("Dune");
		dune.setStatus(Status.CHECKED_OUT);
		itemRepository.save(dune);
		
		Book algernon = new Book();
		algernon.setId(1991);
		algernon.setName("Flowers for Algernon");
		algernon.setStatus(Status.RESERVED);
		itemRepository.save(algernon);
		*/
		
	}

}
