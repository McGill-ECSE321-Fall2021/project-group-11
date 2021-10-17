package ca.mcgill.ecse321.townlibrary.repository;

import ca.mcgill.ecse321.townlibrary.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeadLibrarianRepository extends CrudRepository<HeadLibrarian, Integer> {
/**
 * Finds the instance of Headlibrarian if present
 * from a given Library
 * @param lib The associated Library
 * @return instance of HeadLibrarian or optional
 */
    HeadLibrarian findByLibrary(Library lib);
/**
 * Finds the instance of HeadLibrarian if present
 * from a given address
 * @param address The HeadLibrarian's address
 * @return instance of HeadLibrarian or optional
 */
    HeadLibrarian findByAddress(String address);
}
