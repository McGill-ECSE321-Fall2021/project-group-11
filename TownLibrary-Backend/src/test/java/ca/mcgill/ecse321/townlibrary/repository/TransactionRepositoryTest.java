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
        stTransaction.setStartDate(start);
        stTransaction.setEndDate(end);
        stTransaction.setUserRole(user);
        this.transactionRepository.save(stTransaction);
        Integer stTransactionId = stTransaction.getId();

        // must succeed because we just saved a transaction with this id
        final Transaction ldTransaction = this.transactionRepository.findById(stTransactionId).get();
        Assertions.assertEquals(stTransactionId, ldTransaction.getId());
        Assertions.assertEquals(start, ldTransaction.getStartDate());
        Assertions.assertEquals(end, ldTransaction.getEndDate());
        Assertions.assertEquals(user.getId(), ldTransaction.getUserRole().getId());

        // Test deletes
        this.transactionRepository.delete(ldTransaction);
        Assertions.assertFalse(this.transactionRepository.findById(stTransactionId).isPresent());
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
        Transaction transaction1 = new Transaction();
        transaction1.setUserRole(joe);
        transaction1.setEndDate(new Timestamp(10));
        this.transactionRepository.save(transaction1);
        Integer transaction1Id = transaction1.getId();

        Transaction transaction2 = new Transaction();
        transaction2.setUserRole(joe);
        transaction2.setEndDate(new Timestamp(100));
        this.transactionRepository.save(transaction2);
        Integer transaction2Id = transaction2.getId();

        Transaction transaction3 = new Transaction();
        transaction3.setUserRole(john);
        transaction3.setEndDate(new Timestamp(50));
        this.transactionRepository.save(transaction3);
        Integer transaction3Id = transaction3.getId();

        // Query those dummy transactions
        List<Transaction> ret;
        ret = this.transactionRepository.findByUserRole(joe);
        Assertions.assertEquals(2, ret.size());
        Assertions.assertEquals(
                new HashSet<>(Arrays.asList(transaction1Id, transaction2Id)),
                new HashSet<>(Arrays.asList(ret.get(0).getId(), ret.get(1).getId())));

        ret = this.transactionRepository.findByUserRole(john);
        Assertions.assertEquals(1, ret.size());
        Assertions.assertEquals(transaction3Id, ret.get(0).getId());

        ret = this.transactionRepository.findByUserRoleAndEndDateAfter(joe, new Timestamp(50));
        Assertions.assertEquals(1, ret.size());
        Assertions.assertEquals(transaction2Id, ret.get(0).getId());
    }
    @Test
    public void testSetTransactionType(){
        // Setup some dummy users
        final OfflineMember joe = new OfflineMember();
        joe.setName("Joe Schmoe");
        this.userRoleRepository.save(joe);

        Transaction transaction;
        transaction = new Transaction();
        transaction.setUserRole(joe);
        transaction.setType(TransactionType.books);
        transaction.setEndDate(new Timestamp(10));
        this.transactionRepository.save(transaction);
        Integer transactionId = transaction.getId();
        transaction = null;

        transaction = this.transactionRepository.findById(transactionId).get();
        Assertions.assertNotNull(transaction);
        Assertions.assertEquals(TransactionType.books, transaction.getType());


        transaction.setType(TransactionType.events);
        transaction = this.transactionRepository.findById(transactionId).get();
        Assertions.assertNotNull(transaction);
        Assertions.assertNotEquals(TransactionType.events, transaction.getType());
    }
}
