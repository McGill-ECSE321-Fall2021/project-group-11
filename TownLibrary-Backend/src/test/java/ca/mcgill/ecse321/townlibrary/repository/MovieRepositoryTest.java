package ca.mcgill.ecse321.townlibrary.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.townlibrary.model.*;

import java.util.*;

@SpringBootTest
public class MovieRepositoryTest {
    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private MovieRepository movieRepository;

    @AfterEach
    public void cleanupDB() {
        movieRepository.deleteAll();
        libraryRepository.deleteAll();
    }

    @Test
    public void testPersistMovie() {
        // Setup library for movie
        final Library lib = new Library();
        lib.setId(49);
        libraryRepository.save(lib);

        // Test writes
        final Movie m1 = new Movie();
        m1.setId(70);
        m1.setName("test");
        m1.setStatus(Status.RESERVED);
        m1.setLibrary(lib);
        movieRepository.save(m1);

        // Test if writes were successful
        Assertions.assertEquals(70, m1.getId());
        Assertions.assertEquals("test", m1.getName());
        Assertions.assertEquals(Status.RESERVED, m1.getStatus());
        Assertions.assertEquals(lib.getId(), m1.getLibrary().getId());

        // Test deletes
        movieRepository.delete(m1);
        Assertions.assertTrue(movieRepository.findById(70).isEmpty());
    }

    @Test
    public void testNameAndStatusQueries() {
        // Create movies to test queries
        Movie movie;
        movie = new Movie();
        movie.setId(71);
        movie.setName("t1");
        movie.setStatus(Status.RESERVED);
        movieRepository.save(movie);

        movie = new Movie();
        movie.setId(72);
        movie.setName("t2");
        movie.setStatus(Status.AVAILABLE);
        movieRepository.save(movie);
        
        movie = new Movie();
        movie.setId(73);
        movie.setName("f3");
        movie.setStatus(Status.AVAILABLE);
        movieRepository.save(movie);

        // Test queries
        List<Movie> l;
        l = movieRepository.findByName("t1");
        Assertions.assertEquals(1, l.size());
        Assertions.assertEquals(71, l.get(0).getId());

        l = movieRepository.findByNameContaining("t");
        Assertions.assertEquals(2, l.size());
        Assertions.assertEquals(
                new HashSet<>(Arrays.asList(71, 72)),
                new HashSet<>(Arrays.asList(l.get(0).getId(), l.get(1).getId())));

        l = movieRepository.findByStatus(Status.AVAILABLE);
        Assertions.assertEquals(2, l.size());
        Assertions.assertEquals(
                new HashSet<>(Arrays.asList(72, 73)),
                new HashSet<>(Arrays.asList(l.get(0).getId(), l.get(1).getId())));
    }
}