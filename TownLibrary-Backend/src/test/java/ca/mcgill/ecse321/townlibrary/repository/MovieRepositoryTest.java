package ca.mcgill.ecse321.townlibrary.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.townlibrary.model.*;

import javax.persistence.*;
import java.util.*;

@SpringBootTest
public class MovieRepositoryTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MovieRepository movieRepository;

    @AfterEach
    public void cleanupDB() {
        this.movieRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testPersistMovie() throws Exception {
        final Library lib = new Library();
        entityManager.persist(lib);
        entityManager.flush();

        final Movie m1 = new Movie();
        m1.setId(5);
        m1.setName("test");
        m1.setStatus(Status.RESERVED);
        movieRepository.save(m1);

        Assertions.assertEquals(5, m1.getId());
        Assertions.assertEquals("test", m1.getName());
        Assertions.assertEquals(Status.RESERVED, m1.getStatus());

        movieRepository.delete(m1);
        Assertions.assertTrue(movieRepository.findById(5).isEmpty());

        entityManager.remove(lib);
        entityManager.flush();
    }

    @Test
    public void testNameAndStatusQueries() throws Exception {
        Movie movie;
        movie = new Movie();
        movie.setId(5);
        movie.setName("t1");
        movie.setStatus(Status.RESERVED);
        movieRepository.save(movie);

        movie = new Movie();
        movie.setId(6);
        movie.setName("t2");
        movie.setStatus(Status.AVAILABLE);
        movieRepository.save(movie);
        
        movie = new Movie();
        movie.setId(7);
        movie.setName("f3");
        movie.setStatus(Status.AVAILABLE);
        movieRepository.save(movie);

        List<Movie> l;
        l = movieRepository.findByName("t1");
        Assertions.assertEquals(1, l.size());
        Assertions.assertEquals(5, l.get(0).getId());

        l = movieRepository.findByNameContaining("t");
        Assertions.assertEquals(2, l.size());

        l = movieRepository.findByStatus(Status.AVAILABLE);
        Assertions.assertEquals(2, l.size());
    }
}

