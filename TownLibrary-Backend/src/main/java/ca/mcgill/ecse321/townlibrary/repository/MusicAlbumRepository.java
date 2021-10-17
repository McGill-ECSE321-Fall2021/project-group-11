package ca.mcgill.ecse321.townlibrary.repository;

import ca.mcgill.ecse321.townlibrary.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicAlbumRepository extends CrudRepository<MusicAlbum, Integer>{
    
    /** List all MusicAlbums by their status (Available, Reserved, Checked Out)
     * 
     * @param status    -   Status of music album
     * @return              List of music albums that have that status
     */
    List<MusicAlbum> findByStatus(Status status);

    /** List all MusicAlbums by full name (most likely not needed)
     * 
     * @param name  -   name of music album
     * @return          List of music albums with that name
     */
    List<MusicAlbum> findByName(String name);

    /** List all MusicAlbums by names containing a certain string
     * 
     * @param name  -   name (or just a segment) of music album
     * @return          List of music albums containing that name
     */
    List<MusicAlbum> findByNameContaining(String name);
    
}
