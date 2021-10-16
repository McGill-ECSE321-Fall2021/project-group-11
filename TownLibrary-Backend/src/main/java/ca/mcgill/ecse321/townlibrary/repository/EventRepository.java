package ca.mcgill.ecse321.townlibrary.repository;

import ca.mcgill.ecse321.townlibrary.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<Event, Integer> {
	
	List<Event> findEventById(Integer id);
	List<Event> findByTransaction(Transaction transaction);
    List<Event> findByName(String name);
}
