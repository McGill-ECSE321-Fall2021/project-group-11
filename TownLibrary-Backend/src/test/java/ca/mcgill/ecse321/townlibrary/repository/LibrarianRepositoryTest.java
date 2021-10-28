package ca.mcgill.ecse321.townlibrary.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.townlibrary.model.*;

import java.util.*;

@SpringBootTest
public class LibrarianRepositoryTest {

    @Autowired
    private LibrarianRepository librarianRepository;

    @AfterEach
    public void cleanupDB() {
        librarianRepository.deleteAll();
    }

    @Test
    public void testNameQueries() {
        // Test writes
        final Librarian jeb = new Librarian();
        jeb.setName("Jeb Deb");
        librarianRepository.save(jeb);

        final Librarian jim = new Librarian();
        jim.setName("Jim Bim");
        librarianRepository.save(jim);

        final Librarian kim = new Librarian();
        kim.setName("Kim Keb");
        librarianRepository.save(kim);
        
        List<Librarian> l;
        Librarian lbr;

        // Test queries
        lbr = librarianRepository.findLibrarianById(jeb.getId());
        Assertions.assertEquals(jeb.getId(), lbr.getId());

        l = librarianRepository.findByNameContaining("im");
        Assertions.assertEquals(2, l.size());
        Assertions.assertEquals(
                new HashSet<>(Arrays.asList(jim.getId(), kim.getId())),
                new HashSet<>(Arrays.asList(l.get(0).getId(), l.get(1).getId())));
    }
}