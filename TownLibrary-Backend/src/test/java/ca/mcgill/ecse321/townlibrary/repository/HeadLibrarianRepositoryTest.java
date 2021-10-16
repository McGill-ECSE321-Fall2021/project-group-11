package ca.mcgill.ecse321.townlibrary.repository;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.model.HeadLibrarian;
import ca.mcgill.ecse321.townlibrary.repository.*;
import jdk.jfr.Timestamp;

@SpringBootTest
public class HeadLibrarianRepositoryTest {

    @Autowired
    private LibrarianRepository libraryRepository;

    @Autowired
    private HeadLibrarianRepository headLibrarianRepository;

    @AfterEach
    public void cleanupDB(){
        
        transactionRepository.delete();
        offlineMemberRepository.delete();        
    }
    @Test
    public void testPersistHeadLibrarian(){

        HeadLibrarian hl = new HeadLibrarian();
        String homeAddress = "4201 Wokege";
        hl.setAddress(homeAddress);
        int hlId = 1;
        hl.setId(hlId);
        hl.setName("Dees");
        hl.setLibrary(lib);
        this.headLibrarianRepository.save(hl);


        hl = null;
        hl = offlineMemberRepository.findByAddress("4201 Wokege");

        assertNotNull(hl);
        assertEquals(hlId,hl.getId());
        assertEquals("Dees",hl.getName());

        this.headLibrarianRepository.delete(hl);
        Assertions.assertTrue(this.headLibrarianRepository.
                                findByAddress("4201 Wokege").isEmpty());
    }


    public void testRefLibrary(){
        HeadLibrarian hl = new HeadLibrarian();
        String homeAddress = "4201 Wokege";
        hl.setAddress(homeAddress);
        int hlId = 1;
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

        hl = null;

        hl = this.headLibrarianRepository.findByLibrary(lib);

        assertNotNull(hl);
        assertEquals(hlId, hl.getId());
        assertEquals(hl.getAddress(), lib.getHeadLibrarian().getAddress());
        assertEquals(hl.getName(), lib.getHeadLibrarian().getName());

        this.headLibrarianRepository.delete(hl);
        Assertions.assertNull(this.headLibrarianRepository.findByLibrary(lib));
    }
    
}