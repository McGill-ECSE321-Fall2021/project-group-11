package ca.mcgill.ecse321.townlibrary.repository;

import ca.mcgill.ecse321.townlibrary.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicAlbumRepository extends CrudRepository<MusicAlbum, Integer>{
    
    List<MusicAlbum> findByStatus(Status status);
    List<MusicAlbum> findByName(String name);
    List<MusicAlbum> findByNameContaining(String name);
    
}
