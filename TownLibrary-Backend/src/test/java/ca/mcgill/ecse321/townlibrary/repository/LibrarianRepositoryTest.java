package ca.mcgill.ecse321.townlibrary.repository;

import org.junit.jupiter.api.Test;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.townlibrary.model.*;

import javax.persistence.*;
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
        jeb.setId(5);
        jeb.setName("Jeb Deb");
        librarianRepository.save(jeb);

        final Librarian jim = new Librarian();
        jim.setId(6);
        jim.setName("Jim Bim");
        librarianRepository.save(jim);

        final Librarian kim = new Librarian();
        kim.setId(7);
        kim.setName("Kim Keb");
        librarianRepository.save(kim);
        
        List<Librarian> l;
        l = librarianRepository.findLibrarianById(5);
        Assertions.assertEquals(5, jeb.getId());
        Assertions.assertEquals(1, l.size());

        l = librarianRepository.findByNameContaining("im");
        Assertions.assertEquals(2, l.size());
    }
}