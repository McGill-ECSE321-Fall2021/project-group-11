package ca.mcgill.ecse321.townlibrary.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.repository.LibrarianRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LibrarianServiceTest {

    @Mock
    private LibrarianRepository mockLibrarianRepository;

    @InjectMocks
    private LibrarianService librarianService;

    @Test
    public void testCreateLibrarianNullLib() {
        try {
            this.librarianService.createLibrarian(null, "A", "B");
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("NULL-LIBRARY", ex.getMessage());
        }
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void testCreateLibrarianEmptyName(String name) {
        try {
            this.librarianService.createLibrarian(new Library(), name, "B");
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("EMPTY-NAME", ex.getMessage());
        }
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void testCreateLibrarianEmptyAddress(String address) {
        try {
            this.librarianService.createLibrarian(new Library(), "A", address);
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("EMPTY-ADDRESS", ex.getMessage());
        }
    }

    @Test
    public void testCreateLibrarianEmptyNameAndAddress() {
        // the point is just to show that if mulitple things error, we do
        // get all the causes.

        try {
            this.librarianService.createLibrarian(new Library(), null, null);
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("EMPTY-NAME,EMPTY-ADDRESS", ex.getMessage());
        }
    }

    @Test
    public void testCreateLibrarian() {
        final Library lib = new Library();
        final String name = "Joe Schmoe";
        final String address = "333 Rue University";

        final Librarian librarian = this.librarianService.createLibrarian(lib, name, address);
        Assertions.assertEquals(lib.getId(), librarian.getLibrary().getId());
        Assertions.assertEquals(name, librarian.getName());
        Assertions.assertEquals(address, librarian.getAddress());
    }

    @Test
    public void testGetLibrarian() {
        // Artificially create a situation where only id 0 is bound to a
        // library.
        lenient().when(this.mockLibrarianRepository.findById(0))
                .thenAnswer(invocation -> Optional.of(new Librarian()));

        Librarian u;

        u = this.librarianService.getLibrarian(0);
        Assertions.assertEquals(0, u.getId());

        u = this.librarianService.getLibrarian(4);
        Assertions.assertNull(u);
    }
}
