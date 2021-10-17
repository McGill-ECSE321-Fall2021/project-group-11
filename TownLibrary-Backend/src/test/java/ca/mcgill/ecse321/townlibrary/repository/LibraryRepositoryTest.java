package ca.mcgill.ecse321.townlibrary.repository;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.repository.LibraryRepository;
import ca.mcgill.ecse321.townlibrary.repository.UserRoleRepository;

@SpringBootTest
public class LibraryRepositoryTest {
    
    @Autowired
    private LibraryRepository libraryRepository;

    @AfterEach
    public void clearDatabase(){
        libraryRepository.deleteAll();
    }

    @Test
    public void testPersistLibrary(){
        String address = "845 Rue Sherbrooke";
        Library lib = new Library();
        int libId = 100;
        lib.setId(100);
        lib.setAddress(address);
        this.libraryRepository.save(lib);

        lib = null;

        lib = libraryRepository.findByAddress(address);
        Assertions.assertNotNull(lib);
        Assertions.assertEquals(address, lib.getAddress());

        this.libraryRepository.delete(lib);
        Assertions.assertFalse(this.libraryRepository.findById(libId).isPresent());

    }
    

}
