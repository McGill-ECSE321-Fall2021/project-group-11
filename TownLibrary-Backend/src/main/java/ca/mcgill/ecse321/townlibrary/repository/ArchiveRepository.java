package ca.mcgill.ecse321.townlibrary.repository;

import ca.mcgill.ecse321.townlibrary.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchiveRepository extends CrudRepository<Archive, Integer> {

    List<Archive> findByName(String name);
    List<Archive> findByNameContaining(String name);

    List<Archive> findByStatus(Status status);
}