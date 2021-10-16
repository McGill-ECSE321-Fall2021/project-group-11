package ca.mcgill.ecse321.townlibrary.repository;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.repository.LibraryRepository;
import ca.mcgill.ecse321.townlibrary.repository.TransactionRepository;
import ca.mcgill.ecse321.townlibrary.repository.UserRoleRepository;
import jdk.jfr.Timestamp;

@SpringBootTest
public class OfflineMemberRepositoryTest {

    @Autowired
    private OfflineMemberRepository offlineMemberRepository;

    @Autowired
    private LibraryRepository libraryRepository;

    @AfterEach
    public void cleanupDB(){
        offlineMemberRepository.delete();
        libraryRepository.delete(); 
    }
    @Test
    public void testPersistOfflineMember(){

        String address = "845 Rue Sherbrooke";
        Library lib = new Library();
        lib.setAddress(address);
        this.libraryRepository.save(lib);

        OfflineMember om = new OfflineMember();
        om.setAddress("845 Jacoma");
        int omId = 2;
        om.setId(omId);
        om.setLibrary(lib);
        om.setName("Wu");
        this.offlineMemberRepository.save(om);

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
    
}