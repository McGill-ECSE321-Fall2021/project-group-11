package ca.mcgill.ecse321.townlibrary.repository;

import ca.mcgill.ecse321.townlibrary.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryRepository extends CrudRepository<Library,Integer> {
/**
 * Finds the instance of Library if present
 * from a given address
 * @param address The given address
 * @return instance of Library or optional
 */
    Library findByAddress(String address);
}
