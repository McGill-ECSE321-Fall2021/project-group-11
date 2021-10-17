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
        final Librarian jeb = new Librarian();
        jeb.setId(50);
        jeb.setName("Jeb Deb");
        librarianRepository.save(jeb);

        final Librarian jim = new Librarian();
        jim.setId(51);
        jim.setName("Jim Bim");
        librarianRepository.save(jim);

        final Librarian kim = new Librarian();
        kim.setId(52);
        kim.setName("Kim Keb");
        librarianRepository.save(kim);
        
        List<Librarian> l;
        Librarian lbr;

        lbr = librarianRepository.findLibrarianById(50);
        Assertions.assertEquals(50, lbr.getId());

        l = librarianRepository.findByNameContaining("im");
        Assertions.assertEquals(2, l.size());
    }
}