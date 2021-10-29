package ca.mcgill.ecse321.townlibrary.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.repository.OfflineMemberRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OfflineMemberServiceTest {

    @Mock
    private OfflineMemberRepository mockOfflineMemberRepository;

    @InjectMocks
    private OfflineMemberService offlineMemberService;

    @Test
    public void testCreateOfflineMemberNullLib() {
        try {
            this.offlineMemberService.createOfflineMember(null, "A", "B");
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
            this.offlineMemberService.createOfflineMember(new Library(), name, "B");
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
            this.offlineMemberService.createOfflineMember(new Library(), "A", address);
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
            this.offlineMemberService.createOfflineMember(new Library(), null, null);
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

        final OfflineMember offlineMember = this.offlineMemberService.createOfflineMember(lib, name, address);
        Assertions.assertEquals(lib.getId(), offlineMember.getLibrary().getId());
        Assertions.assertEquals(name, offlineMember.getName());
        Assertions.assertEquals(address, offlineMember.getAddress());
    }

    @ParameterizedTest
    @ValueSource(booleans={ true, false })
    public void testSetOfflineMemberInTownStatus(boolean flag) {
        // Artificially create a situation where only id 0 is bound to a
        // offline member.
        lenient().when(this.mockOfflineMemberRepository.findById(0))
                .thenAnswer(invocation -> Optional.of(new OfflineMember()));

        OfflineMember u;

        u = this.offlineMemberService.setOfflineMemberInTownStatus(0, flag);
        Assertions.assertEquals(flag, u.isInTown());

        try {
            u = this.offlineMemberService.setOfflineMemberInTownStatus(1, flag);
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("NOT-FOUND-OFFLINE-MEMBER", ex.getMessage());
        }
    }

    @Test
    public void testGetOfflineMember() {
        // Artificially create a situation where only id 0 is bound to a
        // offline member.
        lenient().when(this.mockOfflineMemberRepository.findById(0))
                .thenAnswer(invocation -> Optional.of(new OfflineMember()));

        OfflineMember u;

        u = this.offlineMemberService.getOfflineMember(0);
        Assertions.assertEquals(0, u.getId());

        u = this.offlineMemberService.getOfflineMember(4);
        Assertions.assertNull(u);
    }
}
