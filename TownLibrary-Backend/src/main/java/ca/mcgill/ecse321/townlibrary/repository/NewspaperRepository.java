package ca.mcgill.ecse321.townlibrary.repository;

import ca.mcgill.ecse321.townlibrary.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewspaperRepository extends CrudRepository<Newspaper, Integer> {
	
	/**
	 * Searches Newspaper by id.
	 * @param id		The newspaper id.
	 * @return			Newspaper id as an integer.
	 */
	Newspaper findNewspaperById(Integer id);
	
	/**
	 * Searches Newspaper by the exact name.
	 * @param name		The exact name of the newspaper.
	 * @return			A list of newspapers matching the searched name.
	 */
	List<Newspaper> findNewspaperByName(String name);
	
	/**
	 * Searches Newspaper by a fragment of the name.
	 * @param name		The fragment of the newspaper name.
	 * @return			A list of newspapers matching the searched name fragment.
	 */
    List<Newspaper> findNewspaperByNameContaining(String name);
    
    /**
     * Searches Newspaper by its current status.
     * @param status	The status of a newspaper.
     * @return			A list of newspapers matching the searched status.
     */
    List<Newspaper> findNewspaperByStatus(Status status);

}