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

    @AfterEach
    public void cleanupDB(){
        this.musicAlbumRepository.deleteAll();
    }

    @Test
    public void testPersistMusicAlbum(){
        MusicAlbum musicAlbum = new MusicAlbum();
        musicAlbum.setId(1);
        musicAlbum.setName("Donda");
        musicAlbum.setStatus(Status.AVAILABLE);

        this.musicAlbumRepository.save(musicAlbum);

        final MusicAlbum album = this.musicAlbumRepository.findById(1);
        Assertions.assertEquals("Donda", album.getName());
        Assertions.assertEquals(Status.AVAILABLE, album.getStatus());
        Assertions.assertEquals(1, album.getId());
    }

    @Test
    public void testQueryMusicAlbum(){
        MusicAlbum musicAlbum1 = new MusicAlbum();
        musicAlbum1.setId(1);
        musicAlbum1.setName("Donda");
        musicAlbum1.setStatus(Status.AVAILABLE);

        MusicAlbum musicAlbum2 = new MusicAlbum();
        musicAlbum2.setId(2);
        musicAlbum2.setName("CLB");
        musicAlbum2.setStatus(Status.CHECKED_OUT);

        MusicAlbum musicAlbum3 = new MusicAlbum();
        musicAlbum3.setId(3);
        musicAlbum3.setName("WLR");
        musicAlbum3.setStatus(Status.RESERVED);

        MusicAlbum musicAlbum4 = new MusicAlbum();
        musicAlbum4.setId(4);
        musicAlbum4.setName("GINGER");
        musicAlbum4.setStatus(Status.AVAILABLE);

        MusicAlbum musicAlbum5 = new MusicAlbum();
        musicAlbum5.setId(1);
        musicAlbum5.setName("ASTROWORLD");
        musicAlbum5.setStatus(Status.AVAILABLE);

        List<MusicAlbum> result = this.musicAlbumRepository.findByStatus(Status.AVAILABLE);
        Assertions.assertEquals(3, result.size());
        result = this.musicAlbumRepository.findByName("CLB");
        Assertions.assertEquals(1, result.size());
        result = this.musicAlbumRepository.findByNameContaining("L");
        Assertions.assertEquals(3, result.size());

    }

}
