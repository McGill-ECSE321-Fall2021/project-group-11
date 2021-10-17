package ca.mcgill.ecse321.townlibrary.repository;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;

import ca.mcgill.ecse321.townlibrary.model.*;

@SpringBootTest
public class OfflineMemberRepositoryTest {

    @Autowired
    private OfflineMemberRepository offlineMemberRepository;

    @Autowired
    private LibraryRepository libraryRepository;

    @AfterEach
    public void cleanupDB(){
        offlineMemberRepository.deleteAll();
        libraryRepository.deleteAll(); 
    }
    @Test
    public void testPersistOfflineMember(){
        // Create Library Instance
        String address = "845 Rue Sherbrooke";
        Library lib = new Library();
        int libId = 1000;
        lib.setId(libId);
        lib.setAddress(address);
        this.libraryRepository.save(lib);

        // Create offline member to test
        OfflineMember om = new OfflineMember();
        om.setAddress("845 Jacoma");
        int omId = 2;
        om.setId(omId);
        om.setLibrary(lib);
        om.setName("Wu");
        this.offlineMemberRepository.save(om);

        // Test save + read
        om = null;
        om = offlineMemberRepository.findByAddress("845 Jacoma").get(0);
        assertNotNull(om);
        assertEquals(omId, om.getId());
        assertEquals("Wu", om.getName());

        // Test delete
        this.offlineMemberRepository.delete(om);
        Assertions.assertTrue(this.offlineMemberRepository.
                                findByAddress("845 Jacoma").isEmpty());
    }
    
}