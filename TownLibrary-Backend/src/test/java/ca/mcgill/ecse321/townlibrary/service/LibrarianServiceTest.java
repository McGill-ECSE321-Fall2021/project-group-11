package ca.mcgill.ecse321.townlibrary.service;

import org.junit.jupiter.api.Test;
import org.aopalliance.intercept.Invocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.mockito.Spy;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.mockito.invocation.InvocationOnMock;


import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.repository.LibrarianRepository;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class LibrarianServiceTest {

    @Mock
    private LibrarianRepository mockLibrarianRepository;

    @Spy
    private PasswordValidator passwordValidator;

    @InjectMocks
    private LibrarianService librarianService;

    @Test
    public void testCreateLibrarianNullLib() {
        try {
            this.librarianService.createLibrarian(null, "A", "B", "foo1245");
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
            this.librarianService.createLibrarian(new Library(), name, "B", "foo1245");
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
            this.librarianService.createLibrarian(new Library(), "A", address, "foo1245");
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("EMPTY-ADDRESS", ex.getMessage());
        }
    }

    @ParameterizedTest
    @NullSource
    public void testCreateLibrarianEmptyPassword(String password) {
        try {
            this.librarianService.createLibrarian(new Library(), "A", "b", password);
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("EMPTY-PASSWORD", ex.getMessage());
        }
    }

    @Test
    public void testCreateLibrarianEmptyNameAndAddress() {
        // the point is just to show that if mulitple things error, we do
        // get all the causes.

        try {
            this.librarianService.createLibrarian(new Library(), null, null, "foo1245");
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
        final String password = "foo1245";

        final Librarian librarian = this.librarianService.createLibrarian(lib, name, address, password);
        Assertions.assertEquals(lib.getId(), librarian.getLibrary().getId());
        Assertions.assertEquals(name, librarian.getName());
        Assertions.assertEquals(address, librarian.getAddress());
        Assertions.assertEquals(password, librarian.getPassword());
    }

    @Test
    public void testGetLibrarian() {
        // Artificially create a situation where only id 0 is bound to a
        // librarian.
        lenient().when(this.mockLibrarianRepository.findById(0))
                .thenAnswer(invocation -> Optional.of(new Librarian()));

        Librarian u;

        u = this.librarianService.getLibrarian(0);
        Assertions.assertEquals(0, u.getId());

        u = this.librarianService.getLibrarian(4);
        Assertions.assertNull(u);

    }

    @Test
    public void testAuthenticateLibrarian() {
        // Artificially create a situation where only id 0 is bound to a
        // librarian with password abc123.
        final Librarian dummy = new Librarian();
        dummy.setPassword("abc123");
        lenient().when(this.mockLibrarianRepository.findById(0))
                .thenAnswer(invocation -> Optional.of(dummy));

        Assertions.assertTrue(this.librarianService.authenticateLibrarian(0, "abc123"));

        // incorrect password
        Assertions.assertFalse(this.librarianService.authenticateLibrarian(0, "abc1234"));

        // invalid id
        Assertions.assertFalse(this.librarianService.authenticateLibrarian(1, "abc123"));
    }

    @Test
    public void testDeleteExistingLibrarian(){
        
        final Librarian librarian = new Librarian();
        lenient().when(this.mockLibrarianRepository.findById(0)).
                thenReturn(Optional.of(librarian)).thenReturn(Optional.empty());

        final boolean deleted = this.librarianService.deleteLibrarian(0);
        assertTrue(deleted);
    }

    @Test
    public void testDeleteNonExistingLibrarian(){

        lenient().when(this.mockLibrarianRepository.findById(0)).
                thenReturn(Optional.empty());
        try {
            final boolean deleted = this.librarianService.deleteLibrarian(0);   
        } catch (Exception e) {
            assertEquals("LIBRARIAN-NOT-FOUND", e.getMessage());
        }
    }

}
