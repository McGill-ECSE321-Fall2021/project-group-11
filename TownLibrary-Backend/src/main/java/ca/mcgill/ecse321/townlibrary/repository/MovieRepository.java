package ca.mcgill.ecse321.townlibrary.repository;

import ca.mcgill.ecse321.townlibrary.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Integer> {

    /**
	 * Searches Movie by the exact name.
	 * @param name		The exact name of the book.
	 * @return			A list of books matching the searched name.
	 */
    List<Movie> findByName(String name);

    /**
	 * Searches Movie by a section of the name.
	 * @param name		The section of the book name.
	 * @return			A list of movies matching the searched name section.
	 */
    List<Movie> findByNameContaining(String name);

    /**
     * Searches Movie by its current status.
     * @param status	The status of a movie.
     * @return			A list of movies matching the searched status.
     */
    List<Movie> findByStatus(Status status);
}
