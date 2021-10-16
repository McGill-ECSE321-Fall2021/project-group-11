package ca.mcgill.ecse321.townlibrary.repository;

import ca.mcgill.ecse321.townlibrary.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<Event, Integer> {
	
	Event findEventById(Integer id);
	Event findByTransaction(Transaction transaction);
    List<Event> findByNameContaining(String name);
}
