package ca.mcgill.ecse321.townlibrary.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.townlibrary.model.*;

import javax.persistence.*;
import java.util.*;

@SpringBootTest
public class ArchiveRepositoryTest {

    // XXX: remove this when repos for other classes are created!
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ArchiveRepository archiveRepository;

    @AfterEach
    public void cleanupDB() {
        this.archiveRepository.deleteAll();
    }

    @Test
    @Transactional // XXX: same as entity manager
    public void testPersistArchive() {
        // XXX: when accessors are implemented for Item class
        java.lang.reflect.Field field_id = null;
        java.lang.reflect.Field field_status = null;
        java.lang.reflect.Field field_name = null;
        java.lang.reflect.Field field_library = null;
        try {
            field_id = Item.class.getDeclaredField("id");
            field_status = Item.class.getDeclaredField("status");
            field_name = Item.class.getDeclaredField("name");
            field_library = Item.class.getDeclaredField("library");
            field_id.setAccessible(true);
            field_status.setAccessible(true);
            field_name.setAccessible(true);
            field_library.setAccessible(true);
        } catch (Exception ex) {
            Assertions.fail("Reflection issues (should not happen)");
        }

        final Library lib = new Library();
        this.entityManager.persist(lib);
        this.entityManager.flush();

        // Test writes
        final Archive stArchive = new Archive();
        try {
            field_id.setInt(stArchive, 10);
            field_status.set(stArchive, Status.RESERVED);
            field_name.set(stArchive, "FooBar");
            field_library.set(stArchive, lib);
        } catch (Exception ex) {
            Assertions.fail("Reflection issues (should not happen)");
        }
        this.archiveRepository.save(stArchive);

        // this get must succeed!
        final Archive ldArchive = this.archiveRepository.findById(10).get();
        try {
            Assertions.assertEquals(10, field_id.getInt(ldArchive));
            Assertions.assertEquals(Status.RESERVED, field_status.get(stArchive));
            Assertions.assertEquals("FooBar", field_name.get(stArchive));
            Assertions.assertEquals(lib, field_library.get(stArchive));
        } catch (Exception ex) {
            Assertions.fail("Reflection issues (should not happen)");
        }

        // Test deletes
        this.archiveRepository.delete(ldArchive);
        Assertions.assertTrue(this.archiveRepository.findById(10).isEmpty());

        this.entityManager.remove(lib);
        this.entityManager.flush();
    }
}
