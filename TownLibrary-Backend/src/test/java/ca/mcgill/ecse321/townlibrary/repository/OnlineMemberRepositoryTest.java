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

    @AfterEach
    public void cleanupDB(){
        this.onlineMemberRepository.deleteAll();
    }

    @Test
    public void testPersistOnlineMember(){
        final OnlineMember me = new OnlineMember();
        me.setName("A Name");
        me.setEmail("me@mail.ca");
        me.setUsername("myaccount");
        me.setId(1);
        me.setAddress("101 sendhelp avenue");

        this.onlineMemberRepository.save(me);
        
        final OnlineMember onlineMember = this.onlineMemberRepository.findById(1).get();
        Assertions.assertEquals("A Name", onlineMember.getName());
        Assertions.assertEquals("me@mail.ca", onlineMember.getEmail());
        Assertions.assertEquals("myaccount", onlineMember.getUsername());
        Assertions.assertEquals(1, onlineMember.getId());
        Assertions.assertEquals("101 sendhelp avenue", onlineMember.getAddress());
        
    }

    @Test
    public void testQueryOnlineMember(){
        final OnlineMember me = new OnlineMember();
        me.setName("A Name");
        me.setEmail("me@mail.ca");
        me.setUsername("myaccount");
        me.setId(1);
        me.setAddress("101 sendhelp avenue");

        final OnlineMember you = new OnlineMember();
        you.setName("Your Name");
        you.setEmail("you@mail.ca");
        you.setUsername("youraccount");
        you.setId(2);
        you.setAddress("111 somewhere");

        final OnlineMember them = new OnlineMember();
        them.setName("Their Name");
        them.setEmail("them@mail.ca");
        them.setUsername("theiraccount");
        them.setId(3);
        them.setAddress("222 somewhere");

        final OnlineMember same = new OnlineMember();
        same.setName("A Name");
        same.setEmail("same@mail.ca");
        same.setUsername("sameaccount");
        same.setId(4);
        same.setAddress("111 somewhere");

        this.onlineMemberRepository.save(me);
        this.onlineMemberRepository.save(you);
        this.onlineMemberRepository.save(them);
        this.onlineMemberRepository.save(same);

        List<OnlineMember> result = this.onlineMemberRepository.findByAddress("111 somewhere");
        Assertions.assertEquals(2, result.size());
        OnlineMember individualMember = this.onlineMemberRepository.findByEmail("me@mail.ca");
        Assertions.assertEquals("A Name", individualMember.getName());
        individualMember = this.onlineMemberRepository.findById(2).get();
        Assertions.assertEquals("you@mail.ca", individualMember.getEmail());
        
    }
}
