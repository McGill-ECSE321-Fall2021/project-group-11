package ca.mcgill.ecse321.townlibrary.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Spy;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.repository.OnlineMemberRepository;

import java.util.Optional;


import static org.mockito.Mockito.lenient;


@ExtendWith(MockitoExtension.class)
public class OnlineMemberServiceTest {

    @Mock
    private OnlineMemberRepository mockOnlineMemberRepository;

    @Spy
    private EmailValidator emailValidator;

    @Spy
    private PasswordValidator passwordValidator;

    @InjectMocks
    private OnlineMemberService onlineMemberService;

    @Test
    public void testCreateOnlineMemberNullLib() {
        try {
            this.onlineMemberService.createOnlineMember(null, "A", "B", "a.b@ab.com", "aaabbb", "aaabbb");
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("NULL-LIBRARY", ex.getMessage());
        }
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void testCreateOnlineMemberEmptyName(String name) {
        try {
            this.onlineMemberService.createOnlineMember(new Library(), name, "B", "a.b@ab.com", "aaabbb", "aaabbb");
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("EMPTY-NAME", ex.getMessage());
        }
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void testCreateOnlineMemberEmptyAddress(String address) {
        try {
            this.onlineMemberService.createOnlineMember(new Library(), "A", address, "a.b@ab.com", "aaabbb", "aaabbb");
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("EMPTY-ADDRESS", ex.getMessage());
        }
    }

    @ParameterizedTest
    @NullSource
    public void testCreateOnlineMemberEmptyEmail(String email) {
        try {
            this.onlineMemberService.createOnlineMember(new Library(), "A", "b", email, "foobar", "aaabbb");
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("EMPTY-EMAIL", ex.getMessage());
        }
    }

    @Test
    public void testCreateOnlineMemberEmailTaken() {
        // Artificially create a situation where foo@bar.com is already taken.
        final String email = "foo@bar.com";
        lenient().when(this.mockOnlineMemberRepository.findByEmail(email))
                .thenAnswer(invocation -> new OnlineMember());
        try {
            this.onlineMemberService.createOnlineMember(new Library(), "A", "b", email, "foobar", "aaabbb");
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("DUP-EMAIL", ex.getMessage());
        }
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void testCreateOnlineMemberEmptyUsername(String username) {
        try {
            this.onlineMemberService.createOnlineMember(new Library(), "A", "b", "a.b@ab.com", username, "aaabbb");
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("EMPTY-USERNAME", ex.getMessage());
        }
    }

    @Test
    public void testCreateOnlineMemberUsernameTaken() {
        // Artificially create a situation where FooMan123 is already taken.
        final String username = "FooMan123";
        lenient().when(this.mockOnlineMemberRepository.findByUsername(username))
                .thenAnswer(invocation -> new OnlineMember());
        try {
            this.onlineMemberService.createOnlineMember(new Library(), "A", "b", "a.b@ab.com", username, "aaabbb");
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("DUP-USERNAME", ex.getMessage());
        }
    }

    @ParameterizedTest
    @NullSource
    public void testCreateOnlineMemberEmptyPassword(String password) {
        try {
            this.onlineMemberService.createOnlineMember(new Library(), "A", "b", "a.b@ab.com", "foobar", password);
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("EMPTY-PASSWORD", ex.getMessage());
        }
    }

    @Test
    public void testCreateOnlineMemberEmptyNameAndAddress() {
        // the point is just to show that if mulitple things error, we do
        // get all the causes.

        try {
            this.onlineMemberService.createOnlineMember(new Library(), null, null, "a.b@ab.com", "aaabbb", "aaabbb");
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("EMPTY-NAME,EMPTY-ADDRESS", ex.getMessage());
        }
    }

    @Test
    public void testCreateOnlineMember() {
        final Library lib = new Library();
        final String name = "Joe Schmoe";
        final String address = "333 Rue University";
        final String email = "a.b@ab.com";
        final String username = "aaabbbb";
        final String password = "aaabbb";

        final OnlineMember onlineMember = this.onlineMemberService.createOnlineMember(lib, name, address, email, username, password);
        Assertions.assertEquals(lib.getId(), onlineMember.getLibrary().getId());
        Assertions.assertEquals(name, onlineMember.getName());
        Assertions.assertEquals(address, onlineMember.getAddress());
        Assertions.assertEquals(email, onlineMember.getEmail());
        Assertions.assertEquals(username, onlineMember.getUsername());
        Assertions.assertEquals(password, onlineMember.getPassword());
    }

    @ParameterizedTest
    @ValueSource(booleans={ true, false })
    public void testSetOnlineMemberInTownStatus(boolean flag) {
        // Artificially create a situation where only id 0 is bound to a
        // online member.
        lenient().when(this.mockOnlineMemberRepository.findById(0))
                .thenAnswer(invocation -> Optional.of(new OnlineMember()));

        OnlineMember u;

        u = this.onlineMemberService.setOnlineMemberInTownStatus(0, flag);
        Assertions.assertEquals(flag, u.isInTown());

        try {
            u = this.onlineMemberService.setOnlineMemberInTownStatus(1, flag);
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("NOT-FOUND-ONLINE-MEMBER", ex.getMessage());
        }
    }

    @Test
    public void testGetOnlineMember() {
        // Artificially create a situation where only id 0 is bound to a
        // online member.
        lenient().when(this.mockOnlineMemberRepository.findById(0))
                .thenAnswer(invocation -> Optional.of(new OnlineMember()));

        OnlineMember u;

        u = this.onlineMemberService.getOnlineMember(0);
        Assertions.assertEquals(0, u.getId());

        u = this.onlineMemberService.getOnlineMember(4);
        Assertions.assertNull(u);
    }

    @Test
    public void testGetOnlineMemberByUsername() {
        // Artificially create a situation where only FooMan321 is bound to a
        // online member.
        lenient().when(this.mockOnlineMemberRepository.findByUsername("FooMan321"))
                .thenAnswer(invocation -> new OnlineMember());

        OnlineMember u;

        u = this.onlineMemberService.getOnlineMemberByUsername("FooMan321");
        Assertions.assertEquals(0, u.getId());

        u = this.onlineMemberService.getOnlineMemberByUsername("FooMan123");
        Assertions.assertNull(u);
    }
}
