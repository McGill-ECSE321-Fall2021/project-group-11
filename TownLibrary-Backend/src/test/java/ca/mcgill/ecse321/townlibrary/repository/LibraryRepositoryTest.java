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
        libraryRepository.delete();
        UserRoleRepository.delete();
    }

    @Test
    public void testPersistLibrary(){
        String address = "845 Rue Sherbrooke";
        Library lib = new Library();
        lib.setAddress(address);
        libraryRepository.save(lib);

        lib = null;

        lib = libraryRepository.findByAddress(address);
        assertNotNull(lib);
        assertEquals(address, lib.getAddress());
    }
    public void testRefLibrary(){
        HeadLibrarian hl = new HeadLibrarian();
        String homeAddress = "4201 Wokege";
        hl.setAddress(homeAddress);
        hlId = 1;
        hl.setId(hlId);
        hl.setName("Dees");
        hl.setLibrary(lib);
        UserRoleRepository.save(hl);

        String address = "845 Rue Sherbrooke";
        Library lib = new Library();
        libId = 2;
        lib.setId(libId);
        lib.setAddress(address);
        lib.setHeadLibrarian(hl);
        libraryRepository.save(lib);

        lib = null;

        lib = libraryRepository.findByHeadLibrarian(hl);

        assertNotNull(lib);
        assertEquals(libId, lib.getId());
        assertEquals(hl.getAddress(), lib.getHeadLibrarian().getAddress());
        assertEquals(hl.getName(), lib.getHeadLibrarian().getName());
    }

}
