package ca.mcgill.ecse321.townlibrary.repository;

import ca.mcgill.ecse321.townlibrary.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibrarianRepository extends CrudRepository<Librarian, Integer> {
	
	/**
	 * Searches Librarian by id.
	 * @param name		The id of the librarian.
	 * @return			The librarian matching the id.
	 */
	Librarian findLibrarianById(Integer UserId);

	/**
	 * Searches Librarian by a section of the name.
	 * @param name		The section of the librarian name.
	 * @return			A list of librarians matching the searched name section.
	 */
	List<Librarian> findByNameContaining(String name);
}