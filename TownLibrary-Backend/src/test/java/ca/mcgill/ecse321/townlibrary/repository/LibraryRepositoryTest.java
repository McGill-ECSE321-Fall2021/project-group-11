package ca.mcgill.ecse321.townlibrary.repository;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.townlibrary.model.*;

@SpringBootTest
public class LibraryRepositoryTest {

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private HeadLibrarianRepository headLibrarianRepository;

    @AfterEach
    public void clearDatabase(){
        libraryRepository.deleteAll();
        headLibrarianRepository.deleteAll();
    }

    @Test
    public void testPersistLibrary(){
        // Create Library instance
        String address = "845 Rue Sherbrooke";
        Library lib = new Library();
        int libId = 100;
        lib.setId(100);
        lib.setAddress(address);
        this.libraryRepository.save(lib);

        lib = null;

        // Test save + find
        lib = libraryRepository.findByAddress(address);
        Assertions.assertNotNull(lib);
        Assertions.assertEquals(address, lib.getAddress());

        // Test delete
        this.libraryRepository.delete(lib);
        Assertions.assertFalse(this.libraryRepository.findById(libId).isPresent());

    }

    @Test
    public void testPersistLibraryWithHeadLibrarian() {
        // Save the library first
        final Library lib = new Library();
        lib.setId(10100);
        lib.setAddress("855 Rue Sherbrooke");
        this.libraryRepository.save(lib);

        // And then save the head library
        final HeadLibrarian headLibrarian = new HeadLibrarian();
        headLibrarian.setName("Foor");
        headLibrarian.setAddress("865 Rue Sherbrooke");
        headLibrarian.setLibrary(lib);
        this.headLibrarianRepository.save(headLibrarian);

        // And then we update the library (by saving it under the same id)
        lib.setHeadLibrarian(headLibrarian);
        this.libraryRepository.save(lib);

        // And we start testing the fetches
        final Library load = this.libraryRepository.findById(lib.getId()).get();
        Assertions.assertEquals(headLibrarian.getId(), load.getHeadLibrarian().getId());
        Assertions.assertEquals(lib.getId(), headLibrarian.getLibrary().getId());
    }

}
