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
import ca.mcgill.ecse321.townlibrary.repository.OnlineMemberRepository;

@ExtendWith(MockitoExtension.class)
public class OnlineMemberServiceTest {

    @Mock
    private OnlineMemberRepository mockOnlineMemberRepository;

    @Spy
    private EmailValidator emailValidator;

    @Spy
    private PasswordValidator passwordValidator;

    @InjectMocks
    private OnlineMemberService OnlineMemberService;

    @Test
    public void testCreateOnlineMemberNullLib() {
        try {
            this.OnlineMemberService.createOnlineMember(null, "A", "B", "a.b@ab.com", "aaabbb", "aaabbb");
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
            this.OnlineMemberService.createOnlineMember(new Library(), name, "B", "a.b@ab.com", "aaabbb", "aaabbb");
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
            this.OnlineMemberService.createOnlineMember(new Library(), "A", address, "a.b@ab.com", "aaabbb", "aaabbb");
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("EMPTY-ADDRESS", ex.getMessage());
        }
    }

    @ParameterizedTest
    @NullSource
    public void testCreateOnlineMemberEmptyEmail(String email) {
        try {
            this.OnlineMemberService.createOnlineMember(new Library(), "A", "b", email, "foobar", "aaabbb");
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("EMPTY-EMAIL", ex.getMessage());
        }
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void testCreateOnlineMemberEmptyUsername(String username) {
        try {
            this.OnlineMemberService.createOnlineMember(new Library(), "A", "b", "a.b@ab.com", username, "aaabbb");
            Assertions.fail(); // should have thrown
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("EMPTY-USERNAME", ex.getMessage());
        }
    }

    @ParameterizedTest
    @NullSource
    public void testCreateOnlineMemberEmptyPassword(String password) {
        try {
            this.OnlineMemberService.createOnlineMember(new Library(), "A", "b", "a.b@ab.com", "foobar", password);
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
            this.OnlineMemberService.createOnlineMember(new Library(), null, null, "a.b@ab.com", "aaabbb", "aaabbb");
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

        final OnlineMember OnlineMember = this.OnlineMemberService.createOnlineMember(lib, name, address, email, username, password);
        Assertions.assertEquals(lib.getId(), OnlineMember.getLibrary().getId());
        Assertions.assertEquals(name, OnlineMember.getName());
        Assertions.assertEquals(address, OnlineMember.getAddress());
        Assertions.assertEquals(email, OnlineMember.getEmail());
        Assertions.assertEquals(username, OnlineMember.getUsername());
        Assertions.assertEquals(password, OnlineMember.getPassword());
    }
}