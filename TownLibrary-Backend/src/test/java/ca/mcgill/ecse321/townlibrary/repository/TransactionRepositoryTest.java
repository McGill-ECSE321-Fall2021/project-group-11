package ca.mcgill.ecse321.townlibrary.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.townlibrary.model.*;

import javax.persistence.*;
import java.util.*;
import java.sql.Timestamp;

@SpringBootTest
public class TransactionRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private ArchiveRepository archiveRepository;

    @AfterEach
    public void cleanupDB() {
        this.archiveRepository.deleteAll();
        this.eventRepository.deleteAll();
        this.transactionRepository.deleteAll();
        this.userRoleRepository.deleteAll();
    }

    @Test
    public void testPersistTransaction() {
        final Timestamp start = new Timestamp(0);
        final Timestamp end = new Timestamp(10000);
        final OfflineMember user = new OfflineMember();
        final Event event = new Event();
        final Archive archive = new Archive();

        this.userRoleRepository.save(user);
        this.archiveRepository.save(archive);
        this.eventRepository.save(event);

        // Test writes
        final Transaction stTransaction = new Transaction();
        stTransaction.setId(10);
        stTransaction.setStartDate(start);
        stTransaction.setEndDate(end);
        stTransaction.setUserRole(user);
        stTransaction.setEvent(event);
        stTransaction.setItem(archive);
        this.transactionRepository.save(stTransaction);

        // this get must succeed!
        final Transaction ldTransaction = this.transactionRepository.findById(10).get();
        Assertions.assertEquals(10, ldTransaction.getId());
        Assertions.assertEquals(start, ldTransaction.getStartDate());
        Assertions.assertEquals(end, ldTransaction.getEndDate());
        Assertions.assertEquals(user.getId(), ldTransaction.getUserRole().getId());
        Assertions.assertEquals(event.getId(), ldTransaction.getEvent().getId());
        Assertions.assertEquals(archive.getId(), ldTransaction.getItem().getId());

        // Test deletes
        this.transactionRepository.delete(ldTransaction);
        Assertions.assertTrue(this.transactionRepository.findById(10).isEmpty());
    }

    @Test
    public void testUserRoleAndEndDateQueries() {
        final OfflineMember joe = new OfflineMember();
        joe.setId(150);
        joe.setName("Joe Schmoe");
        this.userRoleRepository.save(joe);

        final OfflineMember john = new OfflineMember();
        john.setId(151);
        john.setName("John Doe");
        this.userRoleRepository.save(john);

        Transaction transaction;
        transaction = new Transaction();
        transaction.setId(200);
        transaction.setUserRole(joe);
        transaction.setEndDate(new Timestamp(10));
        this.transactionRepository.save(transaction);

        transaction = new Transaction();
        transaction.setId(201);
        transaction.setUserRole(joe);
        transaction.setEndDate(new Timestamp(100));
        this.transactionRepository.save(transaction);

        transaction = new Transaction();
        transaction.setId(210);
        transaction.setUserRole(john);
        transaction.setEndDate(new Timestamp(50));
        this.transactionRepository.save(transaction);

        List<Transaction> ret;
        ret = this.transactionRepository.findByUserRole(joe);
        Assertions.assertEquals(2, ret.size());
        Assertions.assertEquals(
                new HashSet<>(Arrays.asList(200, 201)),
                new HashSet<>(Arrays.asList(ret.get(0).getId(), ret.get(1).getId())));

        ret = this.transactionRepository.findByUserRole(john);
        Assertions.assertEquals(1, ret.size());
        Assertions.assertEquals(210, ret.get(0).getId());

        ret = this.transactionRepository.findByUserRoleAndEndDateAfter(joe, new Timestamp(50));
        Assertions.assertEquals(1, ret.size());
        Assertions.assertEquals(201, ret.get(0).getId());
    }
}
