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

        String address = "845 Rue Sherbrooke";
        Library lib = new Library();
        lib.setAddress(address);
        libraryRepository.save(lib);

        

        om = null;
        om = offlineMemberRepository.findByAddress("845 Jacoma");
        om = om.get(0);
        assertNotNull(om);
        assertEquals(omId, om.getId());
        assertEquals("Wu", om.getName());

        this.offlineMemberRepository.delete(om);
        Assertions.assertTrue(this.offlineMemberRepository.
                                findByAddress("845 Jacoma").isEmpty());
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
    }
    
}