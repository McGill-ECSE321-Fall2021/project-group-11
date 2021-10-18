package ca.mcgill.ecse321.townlibrary.repository;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

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
    public void testPersistMultipleOfflineMember(){
        // Create Library Instance
        String address = "845 Rue Sherbrooke";
        Library lib = new Library();
        int libId = 1002;
        lib.setId(libId);
        lib.setAddress(address);
        this.libraryRepository.save(lib);

        // Create offline member to test
        OfflineMember om = new OfflineMember();
        om.setAddress("845 Jacoma");
        int omId = 4;
        om.setId(omId);
        om.setLibrary(lib);
        om.setName("Wu");
        this.offlineMemberRepository.save(om);

        // Create another offline member to test
        OfflineMember om1 = new OfflineMember();
        om1.setAddress("845 Jacoma");
        int om1Id = 5;
        om1.setId(om1Id);
        om1.setLibrary(lib);
        om1.setName("Wutang");
        this.offlineMemberRepository.save(om1);

        // Create another offline member to test
        OfflineMember om2 = new OfflineMember();
        om2.setAddress("846 Jacoma");
        int om2Id = 6;
        om2.setId(om2Id);
        om2.setLibrary(lib);
        om2.setName("Wutang");
        this.offlineMemberRepository.save(om2);

        // Test save + find by address for multiple OfflineMembers
        List<OfflineMember> oms = this.offlineMemberRepository.findByAddress("845 Jacoma");
        assertFalse(oms.isEmpty());
        assertEquals(2, oms.size());
        assertEquals(
            new HashSet<>(Arrays.asList(4,5)),
            new HashSet<>(Arrays.asList(oms.get(0).getId(), oms.get(1).getId()))
        );

        oms = null;
        oms = this.offlineMemberRepository.findByNameContaining("Wu");
        assertFalse(oms.isEmpty());
        assertEquals(3, oms.size());
        assertEquals(
            new HashSet<>(Arrays.asList(4,5,6)),
            new HashSet<>(Arrays.asList(oms.get(0).getId(), 
                            oms.get(1).getId(), oms.get(2).getId()))
        );

        this.offlineMemberRepository.deleteAll();
        oms = this.offlineMemberRepository.findByNameContaining("Wu");
        assertTrue(oms.isEmpty());
    
    }
    
}