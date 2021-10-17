package ca.mcgill.ecse321.townlibrary.repository;

import org.junit.jupiter.api.Assertions;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.townlibrary.model.*;

@SpringBootTest
public class OnlineMemberRepositoryTest {

    @Autowired
    private OnlineMemberRepository onlineMemberRepository;

    @Autowired
    private LibraryRepository libraryRepository;

    // clears database
    @AfterEach
    public void cleanupDB(){
        this.onlineMemberRepository.deleteAll();
        this.libraryRepository.deleteAll();
    }

    @Test
    public void testPersistOnlineMember(){
        // Initialize library instance
        Library library = new Library();
        library.setId(50);
        this.libraryRepository.save(library);

        // Initialize an online member, write their attributes, and save to database
        final OnlineMember me = new OnlineMember();
        me.setName("A Name");
        me.setEmail("me@mail.ca");
        me.setUsername("myaccount");
        me.setId(1);
        me.setAddress("101 sendhelp avenue");
        me.setLibrary(library);
        this.onlineMemberRepository.save(me);
        
        // Test to see if attributes persisted
        final OnlineMember onlineMember = this.onlineMemberRepository.findById(1).get();
        Assertions.assertEquals("A Name", onlineMember.getName());
        Assertions.assertEquals("me@mail.ca", onlineMember.getEmail());
        Assertions.assertEquals("myaccount", onlineMember.getUsername());
        Assertions.assertEquals(1, onlineMember.getId());
        Assertions.assertEquals("101 sendhelp avenue", onlineMember.getAddress());
        Assertions.assertEquals(library.getId(), onlineMember.getLibrary().getId());
    }

    @Test
    public void testQueryOnlineMember(){
        // Initialize, write attributes and save to database multiple online members
        final OnlineMember me = new OnlineMember();
        me.setName("A Name");
        me.setEmail("me@mail.ca");
        me.setId(1);
        me.setAddress("101 sendhelp avenue");
        this.onlineMemberRepository.save(me);

        final OnlineMember you = new OnlineMember();
        you.setName("Your Name");
        you.setEmail("you@mail.ca");
        you.setId(2);
        you.setAddress("111 somewhere");
        this.onlineMemberRepository.save(you);

        final OnlineMember them = new OnlineMember();
        them.setName("A Name");
        them.setEmail("their@mail.ca");
        them.setId(3);
        them.setAddress("111 somewhere");
        this.onlineMemberRepository.save(them);
 
        // Test to see correctness of query results
        List<OnlineMember> result = this.onlineMemberRepository.findByAddress("111 somewhere");
        Assertions.assertEquals(2, result.size());
        OnlineMember individualMember = this.onlineMemberRepository.findByEmail("me@mail.ca");
        Assertions.assertEquals("A Name", individualMember.getName());
        individualMember = this.onlineMemberRepository.findById(1).get();
        Assertions.assertEquals("me@mail.ca", individualMember.getEmail());
        
        
    }
}
