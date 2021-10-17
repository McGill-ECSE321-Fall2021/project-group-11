package ca.mcgill.ecse321.townlibrary.repository;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.model.HeadLibrarian;

@SpringBootTest
public class HeadLibrarianRepositoryTest {

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private HeadLibrarianRepository headLibrarianRepository;

    @AfterEach
    public void cleanupDB(){

        this.headLibrarianRepository.deleteAll();
        this.libraryRepository.deleteAll();        
    }
    @Test
    public void testPersistHeadLibrarian(){
        String address = "845 Rue Sherbrooke";
        Library lib = new Library();
        int libId = 2;
        lib.setId(libId);
        lib.setAddress(address);
        this.libraryRepository.save(lib);

        HeadLibrarian hl = new HeadLibrarian();
        String homeAddress = "4201 Wokege";
        hl.setAddress(homeAddress);
        int hlId = 1;
        hl.setId(hlId);
        hl.setName("Dees");
        hl.setLibrary(lib);
        this.headLibrarianRepository.save(hl);


        hl = null;
        hl = this.headLibrarianRepository.findByAddress("4201 Wokege");

        assertNotNull(hl);
        assertEquals(hlId,hl.getId());
        assertEquals("Dees",hl.getName());

        this.headLibrarianRepository.delete(hl);
        Assertions.assertNull(this.headLibrarianRepository.
                                findByAddress("4201 Wokege"));
    }


    public void testRefLibrary(){
        String address = "845 Rue Sherbrooke";
        Library lib = new Library();
        int libId = 2;
        lib.setId(libId);
        lib.setAddress(address);
        this.libraryRepository.save(lib);

        HeadLibrarian hl = new HeadLibrarian();
        String homeAddress = "4201 Wokege";
        hl.setAddress(homeAddress);
        int hlId = 1;
        hl.setId(hlId);
        hl.setName("Dees");
        hl.setLibrary(lib);
        this.headLibrarianRepository.save(hl);

        hl = null;

        hl = this.headLibrarianRepository.findByLibrary(lib);

        assertNotNull(hl);
        assertEquals(hlId, hl.getId());
        assertEquals("Wu", hl.getName());

        this.headLibrarianRepository.delete(hl);
        Assertions.assertNull(this.headLibrarianRepository.findByLibrary(lib));
    }
    
}