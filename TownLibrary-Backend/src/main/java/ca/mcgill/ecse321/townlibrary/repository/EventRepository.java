package ca.mcgill.ecse321.townlibrary.repository;

import ca.mcgill.ecse321.townlibrary.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface EventRepository extends CrudRepository<Event, Integer> {
	
	/**
	 * Searches Event by id.
	 * @param name		The id of the event.
	 * @return			The event matching the id.
	 */
	Event findEventById(Integer id);

	/**
	 * Searches Event by a section of the name.
	 * @param name		The section of the event name.
	 * @return			A list of events matching the searched name section.
	 */
    List<Event> findByNameContaining(String name);

	/**
	 * Searches Event by the attendee.
	 * @param user		The attendee
	 * @return			A set of events with that attendee.
	 */
	Set<Event> findByUsers(UserRole user);
}
