package ca.mcgill.ecse321.townlibrary.repository;

import ca.mcgill.ecse321.townlibrary.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchiveRepository extends CrudRepository<Archive, Integer> {

    /**
     * Searches Archives by the exact name.
     *
     * @param name      The exact name of the Archive.
     *
     * @return          A potentially empty list of Archives.
     */
    List<Archive> findByName(String name);

    /**
     * Searches Archives by a fragment of their name.
     *
     * @param name      The fragment of their name.
     *
     * @return          A potentially empty list of Archives.
     */
    List<Archive> findByNameContaining(String name);

    /**
     * Searches Archives by their current Status.
     *
     * @param status    The status
     *
     * @return          A potentially empty list of Archives.
     */
    List<Archive> findByStatus(Status status);
}