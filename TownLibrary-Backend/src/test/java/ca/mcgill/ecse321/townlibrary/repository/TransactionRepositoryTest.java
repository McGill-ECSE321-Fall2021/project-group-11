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

    // XXX: remove this when repos for other classes are created!
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TransactionRepository transactionRepository;

    @AfterEach
    public void cleanupDB() {
        this.transactionRepository.deleteAll();
    }

    @Test
    @Transactional // XXX: same as entity manager
    public void testPersistTransaction() {
        final Timestamp start = new Timestamp(0);
        final Timestamp end = new Timestamp(10000);
        final OfflineMember user = new OfflineMember();
        final Event event = new Event();
        final Archive archive = new Archive();

        this.entityManager.persist(user);
        this.entityManager.persist(event);
        this.entityManager.persist(archive);
        this.entityManager.flush();

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
        Assertions.assertEquals(user, ldTransaction.getUserRole());
        Assertions.assertEquals(event, ldTransaction.getEvent());
        Assertions.assertEquals(archive, ldTransaction.getItem());

        // Test deletes
        this.transactionRepository.delete(ldTransaction);
        Assertions.assertTrue(this.transactionRepository.findById(10).isEmpty());

        this.entityManager.remove(user);
        this.entityManager.remove(event);
        this.entityManager.remove(archive);
        this.entityManager.flush();
    }
}
