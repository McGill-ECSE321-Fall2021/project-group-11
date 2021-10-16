package ca.mcgill.ecse321.townlibrary.repository;

import ca.mcgill.ecse321.townlibrary.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {
	
	Book findBookById(Integer id);
	List<Book> findByName(String name);
    List<Book> findByNameContaining(String name);
    
    List<Book> findByStatus(Status status);

}