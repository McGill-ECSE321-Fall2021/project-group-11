package ca.mcgill.ecse321.townlibrary.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.mockito.Spy;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.repository.*;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HeadLibrarianServiceTest {

    @Mock
    private HeadLibrarianRepository mockHeadLibrarianRepository;

    @Mock
    private LibraryRepository mockLibraryRepository;

    @Spy
    private PasswordValidator passwordValidator;

    @InjectMocks
    private HeadLibrarianService headLibrarianService;

    @Test
    public void testCreateHeadLibrarianNullLib() {
        try {
            this.headLibrarianService.createHeadLibrarian(null, "A", "B", "foo1245");
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("NULL-LIBRARY", ex.getMessage());
        }
    }

    @Test
    public void testCreateHeadLibrarianDupHeadLibrarian() {
        try {
            // Artificially create a setting where the library already has a
            // head librarian assigned to it.
            final Library lib = new Library();
            lib.setHeadLibrarian(new HeadLibrarian());

            this.headLibrarianService.createHeadLibrarian(lib, "A", "B", "foo1245");
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("DUP-HEAD-LIBRARIAN", ex.getMessage());
        }
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void testCreateHeadLibrarianEmptyName(String name) {
        try {
            this.headLibrarianService.createHeadLibrarian(new Library(), name, "B", "foo1245");
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("EMPTY-NAME", ex.getMessage());
        }
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void testCreateHeadLibrarianEmptyAddress(String address) {
        try {
            this.headLibrarianService.createHeadLibrarian(new Library(), "A", address, "foo1245");
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("EMPTY-ADDRESS", ex.getMessage());
        }
    }

    @ParameterizedTest
    @NullSource
    public void testCreateHeadLibrarianEmptyPassword(String password) {
        try {
            this.headLibrarianService.createHeadLibrarian(new Library(), "A", "B", password);
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("EMPTY-PASSWORD", ex.getMessage());
        }
    }

    @Test
    public void testCreateHeadLibrarianEmptyNameAndAddress() {
        // the point is just to show that if mulitple things error, we do
        // get all the causes.

        try {
            this.headLibrarianService.createHeadLibrarian(new Library(), null, null, "foo1245");
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("EMPTY-NAME,EMPTY-ADDRESS", ex.getMessage());
        }
    }

    @Test
    public void testCreateHeadLibrarian() {
        final Library lib = new Library();
        final String name = "Joe Schmoe";
        final String address = "333 Rue University";
        final String password = "foo1245";

        final HeadLibrarian librarian = this.headLibrarianService.createHeadLibrarian(lib, name, address, password);
        Assertions.assertEquals(lib.getId(), librarian.getLibrary().getId());
        Assertions.assertEquals(name, librarian.getName());
        Assertions.assertEquals(address, librarian.getAddress());
        Assertions.assertEquals(password, librarian.getPassword());
    }

    @Test
    public void testGetHeadLibrarian() {
        // Artificially create a situation where only id 0 is bound to a
        // head librarian.
        lenient().when(this.mockHeadLibrarianRepository.findById(0))
                .thenAnswer(invocation -> Optional.of(new HeadLibrarian()));

        HeadLibrarian u;

        u = this.headLibrarianService.getHeadLibrarian(0);
        Assertions.assertEquals(0, u.getId());

        u = this.headLibrarianService.getHeadLibrarian(4);
        Assertions.assertNull(u);
    }

    @Test
    public void testAuthenticateHeadLibrarian() {
        // Artificially create a situation where only id 0 is bound to a
        // librarian with password abc123.
        final HeadLibrarian dummy = new HeadLibrarian();
        dummy.setPassword("abc123");
        lenient().when(this.mockHeadLibrarianRepository.findById(0))
                .thenAnswer(invocation -> Optional.of(dummy));

        Assertions.assertTrue(this.headLibrarianService.authenticateHeadLibrarian(0, "abc123"));

        // incorrect password
        Assertions.assertFalse(this.headLibrarianService.authenticateHeadLibrarian(0, "abc1234"));

        // invalid id
        Assertions.assertFalse(this.headLibrarianService.authenticateHeadLibrarian(1, "abc123"));
    }
}
