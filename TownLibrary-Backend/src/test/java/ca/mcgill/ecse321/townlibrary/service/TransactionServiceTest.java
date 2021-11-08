package ca.mcgill.ecse321.townlibrary.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.repository.TransactionRepository;

import java.sql.Timestamp;
import java.util.*;

import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository mockTransactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    public void testCreateTransactionNullUser() {
        try {
            this.transactionService.createTransaction(0, new Timestamp(0), new Timestamp(2000), null);
            Assertions.fail();
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("EMPTY-USER", ex.getMessage());
        }
    }

    @Test
    public void testCreateTransaction() {
        final OfflineMember u = new OfflineMember();
        final Transaction t = this.transactionService.createTransaction(10, new Timestamp(0), new Timestamp(2000), u);
        Assertions.assertEquals(10, t.getId());
        Assertions.assertEquals(new Timestamp(0), t.getStartDate());
        Assertions.assertEquals(new Timestamp(2000), t.getEndDate());
        Assertions.assertEquals(0, t.getUserRole().getId());
    }

    @Test
    public void testGetTransaction() {
        // Artificially create a situation where only id 0 is bound to a
        // transaction.
        lenient().when(this.mockTransactionRepository.findById(0))
                .thenAnswer(invocation -> Optional.of(new Transaction()));

        Transaction t;

        t = this.transactionService.getTransaction(0);
        Assertions.assertEquals(0, t.getId());

        t = this.transactionService.getTransaction(4);
        Assertions.assertNull(t);
    }

    @Test
    public void testGetTransactionByUser() {
        final OfflineMember KEY = new OfflineMember();
        KEY.setId(1); // say
        final Transaction VALUE = new Transaction();
        VALUE.setUserRole(KEY);
        lenient().when(this.mockTransactionRepository.findByUserRole(KEY))
                .thenAnswer(invocation -> Collections.singletonList(VALUE));

        List<Transaction> ts;

        ts = this.transactionService.getTransactionsByUser(KEY);
        Assertions.assertEquals(1, ts.size());
        Assertions.assertEquals(KEY.getId(), ts.get(0).getUserRole().getId());
    }
}