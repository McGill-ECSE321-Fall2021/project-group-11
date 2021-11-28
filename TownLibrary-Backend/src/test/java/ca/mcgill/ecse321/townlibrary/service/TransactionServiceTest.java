package ca.mcgill.ecse321.townlibrary.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import ca.mcgill.ecse321.townlibrary.repository.*;
import ca.mcgill.ecse321.townlibrary.model.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
	
	@Mock
	TransactionRepository transactionDao;

	@Mock 
	ItemRepository itemDao;
	
	@InjectMocks
	private TransactionService service;
	private static final Transaction INVALID_TRANSACTION = new Transaction();
	private static final Transaction VALID_TRANSACTION = new Transaction();
	private static final UserRole USER = new OfflineMember();
	private static final TransactionType TYPE = TransactionType.books;
	private static final Timestamp START_TIME = new Timestamp(System.currentTimeMillis() - 1000 * 86400 * 11);
	private static final Timestamp END_TIME = new Timestamp(System.currentTimeMillis() + 1000 * 86400 * 3);
	
	@BeforeEach
	public void setMockOutput() {
		lenient().when(transactionDao.findByUserRole(any(UserRole.class))).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(USER)) {
				Transaction transaction = new Transaction();
				transaction.setUserRole(USER);
				
				List<Transaction> tList = new ArrayList<Transaction>();
				tList.add(transaction);
				return tList;
			}
			else {
				return new ArrayList<Transaction>();
			}
		});
		lenient().when(itemDao.findItemByTransaction(any(Transaction.class))).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(INVALID_TRANSACTION)){
				INVALID_TRANSACTION.setEndDate(new Timestamp (System.currentTimeMillis() + 1000 * 86400 * 8));
				Item item = new Book();
				item.setStatus(Status.AVAILABLE);
				item.setTransaction(INVALID_TRANSACTION);
				return item;
			} else if(invocation.getArgument(0).equals(VALID_TRANSACTION)) {
				VALID_TRANSACTION.setUserRole(USER);
				VALID_TRANSACTION.setStartDate(START_TIME);
				VALID_TRANSACTION.setEndDate(END_TIME);
				VALID_TRANSACTION.setType(TYPE);
				Item item = new Book();
				item.setStatus(Status.CHECKED_OUT);
				item.setTransaction(VALID_TRANSACTION);
				return item;
			} else {
				return null;
			}
		});
		Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
			return invocation.getArgument(0);
		};
		lenient().when(itemDao.save(any(Item.class))).thenAnswer(returnParameterAsAnswer);
		lenient().when(transactionDao.save(any(Transaction.class))).thenAnswer(returnParameterAsAnswer);
	}
	
	@Test
	public void testCreateTransaction() {

		try {
			service.createTransaction( null, END_TIME, USER, TYPE);
		} catch (IllegalArgumentException e) {
			assertEquals("Transaction start time cannot be empty.", e.getMessage());
		}
		
		try {
			service.createTransaction(START_TIME, null, USER, TYPE);
		} catch (IllegalArgumentException e) {
			assertEquals("Transaction end time cannot be empty.", e.getMessage());
		}
		
		try {
			service.createTransaction( START_TIME, START_TIME, USER, TYPE);
		} catch (IllegalArgumentException e) {
			assertEquals("Transaction end time cannot be before start time.", e.getMessage());
		}
		
		try {
			service.createTransaction( END_TIME, START_TIME, USER, TYPE);
		} catch (IllegalArgumentException e) {
			assertEquals("Transaction end time cannot be before start time.", e.getMessage());
		}
		
		try {
			service.createTransaction( START_TIME, END_TIME, null, TYPE);
		} catch (IllegalArgumentException e) {
			assertEquals("User cannot be empty.", e.getMessage());
		}

		try {
			service.createTransaction( START_TIME, END_TIME, USER, null);
		} catch (IllegalArgumentException e) {
			assertEquals("Type cannot be empty.", e.getMessage());
		}
		
	}
	
	@Test
	public void testGetTransactionByNullUser() {
		assertEquals(1, service.getTransactionsByUser(USER).size());
	
		try {
			service.getTransactionsByUser(null);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("User cannot be empty.", e.getMessage());
		}
	}
	@Test
	public void testGetTransactionByValidUser(){

		try {
			final List<Transaction> transactions = service.getTransactionsByUser(USER);
			assertNotEquals(transactions.size(), 0);
		} catch (Exception e) {
			fail();
		}

	}
	@Test 
	public void testRenewTransactionInvalidTime(){
		try {
			itemDao.findItemByTransaction(INVALID_TRANSACTION);
			service.renewTransaction(INVALID_TRANSACTION);
			fail();
		} catch (Exception e) {
			assertEquals("OUT-OF-TIMEFRAME", e.getMessage());
		}
	}

	@Test 
	public void testRenewTransactionValidItem(){
		try {
			itemDao.findItemByTransaction(VALID_TRANSACTION);
			Transaction transaction = service.renewTransaction(VALID_TRANSACTION);
			assertEquals(transaction.getStartDate().getTime(), VALID_TRANSACTION.getStartDate().getTime());
			assertEquals(transaction.getEndDate().getTime() - 1000 * 86400 * 14, END_TIME.getTime());
		} catch (Exception e) {
			fail();
		}
	}
	@Test
	public void testReturnExistingTransaction(){
		
		lenient().when(this.transactionDao.findById(VALID_TRANSACTION.getId())).
                thenReturn(Optional.of(VALID_TRANSACTION)).thenReturn(Optional.empty());
		try {
			Boolean deleted = service.returnTransaction(INVALID_TRANSACTION.getId());
			assertTrue(deleted);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testReturnNonExistantTransaction(){

		lenient().when(this.transactionDao.findById(INVALID_TRANSACTION.getId())).
            thenReturn(Optional.empty());

		try {
			Boolean deleted = service.returnTransaction(INVALID_TRANSACTION.getId());
			fail();
		} catch (Exception e) {
			Assertions.assertEquals("TRANSACTION-NOT-FOUND", e.getMessage());
		}
	}

}
