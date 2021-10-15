package ca.mcgill.ecse321.townlibrary.repository;

import ca.mcgill.ecse321.townlibrary.model.*;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Integer> {
	
	List<Book> findByName(String name);
    List<Book> findByNameContaining(String name);
    
    List<Book> findByStatus(Status status);

}