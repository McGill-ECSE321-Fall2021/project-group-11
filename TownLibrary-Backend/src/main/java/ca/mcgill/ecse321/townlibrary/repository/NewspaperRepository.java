package ca.mcgill.ecse321.townlibrary.repository;

import ca.mcgill.ecse321.townlibrary.model.*;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NewspaperRepository extends CrudRepository<Newspaper, Integer> {
	
	List<Newspaper> findByName(String name);
    List<Newspaper> findByNameContaining(String name);

}