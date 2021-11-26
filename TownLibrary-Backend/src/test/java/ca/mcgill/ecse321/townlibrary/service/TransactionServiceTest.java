package ca.mcgill.ecse321.townlibrary.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.mcgill.ecse321.townlibrary.repository.*;
import ca.mcgill.ecse321.townlibrary.model.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
	
	@Mock
	TransactionRepository transactionDao;
	
	@InjectMocks
	private TransactionService service;
	private static final UserRole USER = new OfflineMember();
	private static final TransactionType TYPE = TransactionType.books;
	private static final Timestamp START_TIME = new Timestamp(System.currentTimeMillis());
	private static final Timestamp END_TIME = new Timestamp(START_TIME.getTime() + 1000 * 86400 * 14);
	
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

}
