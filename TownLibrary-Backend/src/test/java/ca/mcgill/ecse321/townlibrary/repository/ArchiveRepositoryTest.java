package ca.mcgill.ecse321.townlibrary.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.townlibrary.model.*;

import javax.persistence.*;
import java.util.*;

@SpringBootTest
public class ArchiveRepositoryTest {

    // XXX: remove this when accessors are implemented for Item class
    private static java.lang.reflect.Field field_id;
    private static java.lang.reflect.Field field_status;
    private static java.lang.reflect.Field field_name;
    private static java.lang.reflect.Field field_library;

    @BeforeAll
    public static void temporaryReflectionMagic() throws Exception {
        field_id = Item.class.getDeclaredField("id");
        field_status = Item.class.getDeclaredField("status");
        field_name = Item.class.getDeclaredField("name");
        field_library = Item.class.getDeclaredField("library");

        field_id.setAccessible(true);
        field_status.setAccessible(true);
        field_name.setAccessible(true);
        field_library.setAccessible(true);
    }

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
    public void testPersistArchive() throws Exception {
        final Library lib = new Library();
        this.entityManager.persist(lib);
        this.entityManager.flush();

        // Test writes
        final Archive stArchive = new Archive();
        field_id.setInt(stArchive, 10);
        field_status.set(stArchive, Status.RESERVED);
        field_name.set(stArchive, "FooBar");
        field_library.set(stArchive, lib);
        this.archiveRepository.save(stArchive);

        // this get must succeed!
        final Archive ldArchive = this.archiveRepository.findById(10).get();
        Assertions.assertEquals(10, field_id.getInt(ldArchive));
        Assertions.assertEquals(Status.RESERVED, field_status.get(stArchive));
        Assertions.assertEquals("FooBar", field_name.get(stArchive));
        Assertions.assertEquals(lib, field_library.get(stArchive));

        // Test deletes
        this.archiveRepository.delete(ldArchive);
        Assertions.assertTrue(this.archiveRepository.findById(10).isEmpty());

        this.entityManager.remove(lib);
        this.entityManager.flush();
    }
}
