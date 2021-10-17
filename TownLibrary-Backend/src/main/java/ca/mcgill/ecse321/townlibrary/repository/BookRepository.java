package ca.mcgill.ecse321.townlibrary.repository;

import ca.mcgill.ecse321.townlibrary.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {
	
	/**
	 * Searches Book by id.
	 * @param id		The book id.
	 * @return			Book id as an integer.
	 */
	Book findBookById(Integer id);
	
	/**
	 * Searches Book by the exact name.
	 * @param name		The exact name of the book.
	 * @return			A list of books matching the searched name.
	 */
	List<Book> findByName(String name);
	
	/**
	 * Searches Book by a fragment of the name.
	 * @param name		The fragment of the book name.
	 * @return			A list of books matching the searched name fragment.
	 */
    List<Book> findByNameContaining(String name);
    
    /**
     * Searches Book by its current status.
     * @param status	The status of a book.
     * @return			A list of books matching the searched status.
     */
    List<Book> findByStatus(Status status);

}