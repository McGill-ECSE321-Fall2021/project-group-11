package ca.mcgill.ecse321.townlibrary.repository;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.townlibrary.model.*;

@SpringBootTest
public class MusicAlbumRepositoryTest {
    @Autowired
    private MusicAlbumRepository musicAlbumRepository;

    @Autowired
    private LibraryRepository libraryRepository;

    // clears database
    @AfterEach
    public void cleanupDB(){
        this.musicAlbumRepository.deleteAll();
        this.libraryRepository.deleteAll();
    }

    @Test
    public void testPersistMusicAlbum(){
        // Initialize library instance
        Library library = new Library();
        library.setId(50);
        this.libraryRepository.save(library);

        // Initialize a music album, write attritbutes, and save to database
        MusicAlbum musicAlbum = new MusicAlbum();
        musicAlbum.setId(1);
        musicAlbum.setName("Donda");
        musicAlbum.setStatus(Status.RESERVED);
        musicAlbum.setLibrary(library);
        this.musicAlbumRepository.save(musicAlbum);

        // Test to see if attributes persisted
        final MusicAlbum album = this.musicAlbumRepository.findById(1).get();
        Assertions.assertEquals("Donda", album.getName());
        Assertions.assertEquals(Status.RESERVED, album.getStatus());
        Assertions.assertEquals(1, album.getId());
        Assertions.assertEquals(library.getId(), musicAlbum.getLibrary().getId());
    }

    @Test
    public void testQueryMusicAlbum(){
        // Initialize multiple albums and save them to database
        MusicAlbum musicAlbum1 = new MusicAlbum();
        musicAlbum1.setId(1);
        musicAlbum1.setName("Donda");
        musicAlbum1.setStatus(Status.AVAILABLE);
        this.musicAlbumRepository.save(musicAlbum1);

        MusicAlbum musicAlbum2 = new MusicAlbum();
        musicAlbum2.setId(2);
        musicAlbum2.setName("CLB");
        musicAlbum2.setStatus(Status.CHECKED_OUT);
        this.musicAlbumRepository.save(musicAlbum2);

        MusicAlbum musicAlbum3 = new MusicAlbum();
        musicAlbum3.setId(3);
        musicAlbum3.setName("GINGER");
        musicAlbum3.setStatus(Status.AVAILABLE);
        this.musicAlbumRepository.save(musicAlbum3);

        // Test to see if the right albums are returned given a query
        List<MusicAlbum> persistMusicAlbums = this.musicAlbumRepository.findByStatus(Status.AVAILABLE);
        Assertions.assertEquals(2, persistMusicAlbums.size());
        persistMusicAlbums = this.musicAlbumRepository.findByName("CLB");
        Assertions.assertEquals(1, persistMusicAlbums.size());
        persistMusicAlbums = this.musicAlbumRepository.findByNameContaining("L");
        Assertions.assertEquals(1, persistMusicAlbums.size());
    }

    @Test
    public void testDeleteMusicAlbum(){
        // Initialize multiple albums and save them to database
        MusicAlbum musicAlbum1 = new MusicAlbum();
        musicAlbum1.setId(1);
        musicAlbum1.setName("Donda");
        musicAlbum1.setStatus(Status.AVAILABLE);
        this.musicAlbumRepository.save(musicAlbum1);
 
        MusicAlbum musicAlbum2 = new MusicAlbum();
        musicAlbum2.setId(2);
        musicAlbum2.setName("CLB");
        musicAlbum2.setStatus(Status.AVAILABLE);
        this.musicAlbumRepository.save(musicAlbum2);

        // Test if delete works
        List<MusicAlbum> persistMusicAlbums = this.musicAlbumRepository.findByStatus(Status.AVAILABLE);
        Assertions.assertEquals(2, persistMusicAlbums.size());

        // Delete one music album from two
        this.musicAlbumRepository.delete(musicAlbum1);
        persistMusicAlbums = this.musicAlbumRepository.findByStatus(Status.AVAILABLE);
        Assertions.assertEquals(1, persistMusicAlbums.size());
    }

}
