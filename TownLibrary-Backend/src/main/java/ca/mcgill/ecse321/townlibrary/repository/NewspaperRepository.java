package ca.mcgill.ecse321.townlibrary.repository;

import ca.mcgill.ecse321.townlibrary.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewspaperRepository extends CrudRepository<Newspaper, Integer> {
	
	Newspaper findNewspaperById(Integer id);
	List<Newspaper> findNewspaperByName(String name);
    List<Newspaper> findNewspaperByNameContaining(String name);
    
    List<Book> findNewspaperByStatus(Status status);

}