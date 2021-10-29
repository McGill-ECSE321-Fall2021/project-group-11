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
import ca.mcgill.ecse321.townlibrary.repository.OfflineMemberRepository;

@ExtendWith(MockitoExtension.class)
public class OfflineMemberServiceTest {

    @Mock
    private OfflineMemberRepository mockOfflineMemberRepository;

    @InjectMocks
    private OfflineMemberService OfflineMemberService;

    @Test
    public void testCreateOfflineMemberNullLib() {
        try {
            this.OfflineMemberService.createOfflineMember(null, "A", "B");
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("NULL-LIBRARY", ex.getMessage());
        }
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void testCreateOfflineMemberEmptyName(String name) {
        try {
            this.OfflineMemberService.createOfflineMember(new Library(), name, "B");
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("EMPTY-NAME", ex.getMessage());
        }
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void testCreateOfflineMemberEmptyAddress(String address) {
        try {
            this.OfflineMemberService.createOfflineMember(new Library(), "A", address);
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("EMPTY-ADDRESS", ex.getMessage());
        }
    }

    @Test
    public void testCreateOfflineMemberEmptyNameAndAddress() {
        // the point is just to show that if mulitple things error, we do
        // get all the causes.

        try {
            this.OfflineMemberService.createOfflineMember(new Library(), null, null);
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("EMPTY-NAME,EMPTY-ADDRESS", ex.getMessage());
        }
    }

    @Test
    public void testCreateOfflineMember() {
        final Library lib = new Library();
        final String name = "Joe Schmoe";
        final String address = "333 Rue University";

        final OfflineMember OfflineMember = this.OfflineMemberService.createOfflineMember(lib, name, address);
        Assertions.assertEquals(lib.getId(), OfflineMember.getLibrary().getId());
        Assertions.assertEquals(name, OfflineMember.getName());
        Assertions.assertEquals(address, OfflineMember.getAddress());
    }
}
