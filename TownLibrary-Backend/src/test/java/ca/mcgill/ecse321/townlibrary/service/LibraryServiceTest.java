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
import ca.mcgill.ecse321.townlibrary.repository.LibraryRepository;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LibraryServiceTest {

    @Mock
    private LibraryRepository mockHeadLibraryRepository;

    @InjectMocks
    private LibraryService libraryService;

    @Test
    public void testCreateLibraryDuplicate() {
        // Artificially create a situation where every possible library id is
        // taken (which is probably impossible, but for the sake of testing)
        lenient().when(this.mockHeadLibraryRepository.existsById(anyInt()))
                .thenAnswer(invocation -> true);

        try {
            this.libraryService.createLibrary(0, "abc");
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("DUP-LIBRARY", ex.getMessage());
        }
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void testCreateLibraryEmptyAddress(String address) {
        try {
            this.libraryService.createLibrary(0, address);
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("EMPTY-ADDRESS", ex.getMessage());
        }
    }

    @Test
    public void testCreateLibraryBadIdAndAddress() {
        // the point is just to show that if mulitple things error, we do
        // get all the causes.

        // Artificially create a situation where every possible library id is
        // taken (which is probably impossible, but for the sake of testing)
        lenient().when(this.mockHeadLibraryRepository.existsById(anyInt()))
                .thenAnswer(invocation -> true);

        try {
            this.libraryService.createLibrary(0, null);
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("DUP-LIBRARY,EMPTY-ADDRESS", ex.getMessage());
        }
    }

    @Test
    public void testCreateLibrary() {
        final int id = 0;
        final String address = "999 Rue Quebec";
        this.libraryService.createLibrary(id, address);

        final Library lib = this.libraryService.createLibrary(id, address);
        Assertions.assertEquals(id, lib.getId());
        Assertions.assertEquals(address, lib.getAddress());
    }
}
