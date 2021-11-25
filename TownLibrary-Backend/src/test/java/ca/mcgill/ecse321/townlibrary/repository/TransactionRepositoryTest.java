package ca.mcgill.ecse321.townlibrary.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.townlibrary.model.*;

import java.util.*;
import java.sql.Timestamp;

@SpringBootTest
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @AfterEach
    public void cleanupDB() {
        this.transactionRepository.deleteAll();
        this.userRoleRepository.deleteAll();
    }

    @Test
    public void testPersistTransaction() {
        // Setup some fields that will be saved along with our Transaction
        final Timestamp start = new Timestamp(0);
        final Timestamp end = new Timestamp(10000);
        final OfflineMember user = new OfflineMember();
        this.userRoleRepository.save(user);

        // Test writes
        final Transaction stTransaction = new Transaction();
        stTransaction.setId(10);
        stTransaction.setStartDate(start);
        stTransaction.setEndDate(end);
        stTransaction.setUserRole(user);
        this.transactionRepository.save(stTransaction);

        // must succeed because we just saved a transaction with this id
        final Transaction ldTransaction = this.transactionRepository.findById(10).get();
        Assertions.assertEquals(10, ldTransaction.getId());
        Assertions.assertEquals(start, ldTransaction.getStartDate());
        Assertions.assertEquals(end, ldTransaction.getEndDate());
        Assertions.assertEquals(user.getId(), ldTransaction.getUserRole().getId());

        // Test deletes
        this.transactionRepository.delete(ldTransaction);
        Assertions.assertFalse(this.transactionRepository.findById(10).isPresent());
    }

    @Test
    public void testUserRoleAndEndDateQueries() {
        // Setup some dummy users
        final OfflineMember joe = new OfflineMember();
        joe.setName("Joe Schmoe");
        this.userRoleRepository.save(joe);

        final OfflineMember john = new OfflineMember();
        john.setName("John Doe");
        this.userRoleRepository.save(john);

        // Setup some dummy transactions (associated to the dummy users)
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

        // Query those dummy transactions
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
    @Test
    public void testSetTransactionType(){
        // Setup some dummy users
        final OfflineMember joe = new OfflineMember();
        joe.setName("Joe Schmoe");
        this.userRoleRepository.save(joe);

        int transactionId = 200;
        Transaction transaction;
        transaction = new Transaction();
        transaction.setId(transactionId);
        transaction.setUserRole(joe);
        transaction.setType(TransactionType.Item);
        transaction.setEndDate(new Timestamp(10));
        this.transactionRepository.save(transaction);

        transaction = null;

        transaction = this.transactionRepository.findById(transactionId).get();
        Assertions.assertNotNull(transaction);
        Assertions.assertEquals(TransactionType.Item, transaction.getType());


        transaction.setType(TransactionType.Event);
        transaction = this.transactionRepository.findById(transactionId).get();
        Assertions.assertNotNull(transaction);
        Assertions.assertNotEquals(TransactionType.Event, transaction.getType());
    }
}
