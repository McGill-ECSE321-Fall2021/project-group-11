package ca.mcgill.ecse321.townlibrary.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.townlibrary.model.*;

import javax.persistence.*;
import java.util.*;

@SpringBootTest
public class ArchiveRepositoryTest {

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private ArchiveRepository archiveRepository;

    @AfterEach
    public void cleanupDB() {
        this.archiveRepository.deleteAll();
        this.libraryRepository.deleteAll();
    }

    @Test
    public void testPersistArchive() throws Exception {
        final Library lib = new Library();
        lib.setId(156);
        this.libraryRepository.save(lib);

        // Test writes
        final Archive stArchive = new Archive();
        stArchive.setId(10);
        stArchive.setStatus(Status.RESERVED);
        stArchive.setName("FooBar");
        stArchive.setLibrary(lib);
        this.archiveRepository.save(stArchive);

        // this get must succeed!
        final Archive ldArchive = this.archiveRepository.findById(10).get();
        Assertions.assertEquals(10, ldArchive.getId());
        Assertions.assertEquals(Status.RESERVED, ldArchive.getStatus());
        Assertions.assertEquals("FooBar", ldArchive.getName());
        Assertions.assertEquals(lib.getId(), ldArchive.getLibrary().getId());

        // Test deletes
        this.archiveRepository.delete(ldArchive);
        Assertions.assertTrue(this.archiveRepository.findById(10).isEmpty());
    }

    @Test
    public void testNameAndStatusQueries() throws Exception {
        Archive archive;
        archive = new Archive();
        archive.setId(100);
        archive.setName("Foo");
        archive.setStatus(Status.AVAILABLE);
        this.archiveRepository.save(archive);

        archive = new Archive();
        archive.setId(102);
        archive.setName("Bar");
        archive.setStatus(Status.AVAILABLE);
        this.archiveRepository.save(archive);

        archive = new Archive();
        archive.setId(103);
        archive.setName("Baz");
        archive.setStatus(Status.RESERVED);
        this.archiveRepository.save(archive);

        List<Archive> ret;
        ret = this.archiveRepository.findByName("Foo");
        Assertions.assertEquals(1, ret.size());
        Assertions.assertEquals(100, ret.get(0).getId());

        ret = this.archiveRepository.findByName("Bar");
        Assertions.assertEquals(1, ret.size());
        Assertions.assertEquals(102, ret.get(0).getId());

        ret = this.archiveRepository.findByName("Baz");
        Assertions.assertEquals(1, ret.size());
        Assertions.assertEquals(103, ret.get(0).getId());

        ret = this.archiveRepository.findByNameContaining("Ba");
        Assertions.assertEquals(2, ret.size());
        Assertions.assertEquals(
                new HashSet<>(Arrays.asList(102 /* Bar */, 103 /* Baz */)),
                new HashSet<>(Arrays.asList(ret.get(0).getId(), ret.get(1).getId())));

        ret = this.archiveRepository.findByStatus(Status.AVAILABLE);
        Assertions.assertEquals(2, ret.size());
        Assertions.assertEquals(
                new HashSet<>(Arrays.asList(100 /* Foo */, 102 /* Bar */)),
                new HashSet<>(Arrays.asList(ret.get(0).getId(), ret.get(1).getId())));
    }
}
