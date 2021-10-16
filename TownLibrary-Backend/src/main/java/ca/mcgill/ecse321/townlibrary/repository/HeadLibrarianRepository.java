package ca.mcgill.ecse321.townlibrary.repository;

import ca.mcgill.ecse321.townlibrary.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeadLibrarian extends CrudRepository<Archive, Integer> {

    List<HeadLibrarian> findByLibrary(String name);
    List<HeadLibrarian> findById(int id);
}
