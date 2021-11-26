package ca.mcgill.ecse321.townlibrary.service;

import java.util.List;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.townlibrary.repository.*;
import ca.mcgill.ecse321.townlibrary.model.*;

@Service
public class TransactionService {

	@Autowired
	TransactionRepository transactionRepository;

	@Autowired 
	ItemRepository itemRepository;

	@Transactional
	public Transaction createTransaction(Timestamp start, Timestamp end, UserRole user, TransactionType type) {

		String error = "";
		if (start == null) {
			error = error + "Transaction start time cannot be empty.";
		}
		if (end == null) {
			error = error + "Transaction end time cannot be empty.";
		}
		if (end != null && start != null && end.before(start)) {
	        error = error + "Transaction end time cannot be before start time.";
	    }
		if (user == null) {
			error = error + "User cannot be empty.";
		}
		if (type == null) {
			error = error + "Type cannot be empty.";
		}

		error = error.trim();
	    if (error.length() > 0) {
	        throw new IllegalArgumentException(error);
	    }

		Transaction transaction = new Transaction();
		transaction.setStartDate(start);
		transaction.setEndDate(end);
		transaction.setUserRole(user);
		transaction.setType(type);
		transactionRepository.save(transaction);
		return transaction;
	}

	
	@Transactional
   public Transaction getTransaction(int id) {
		if (id < 0) {
	        throw new IllegalArgumentException("Unsupported Id.");
	    }
       return this.transactionRepository.findById(id).orElse(null);
   }
	
	@Transactional
	public List<Transaction> getTransactionsByUser(UserRole user) {
		if (user == null) {
	        throw new IllegalArgumentException("User cannot be empty.");
	    }
		return transactionRepository.findByUserRole(user);
	}

	@Transactional
	public Transaction renewTransaction(Transaction oldTransaction){
		
		Transaction newTransaction = new Transaction();

		Item item = itemRepository.findItemByTransaction(oldTransaction);
		if (!item.getStatus().equals(Status.CHECKED_OUT)) {
			throw new IllegalArgumentException("NOT-CHECKED-OUT");
		}
		Timestamp oldEndDate = oldTransaction.getEndDate();
		
		Timestamp newStartDate = oldEndDate;
		Timestamp newEndDate = new Timestamp(oldEndDate.getTime() + 1000 * 86400 * 14);

		newTransaction = createTransaction(newStartDate, newEndDate, oldTransaction.getUserRole(), oldTransaction.getType());

		return newTransaction;
	}
	@Transactional
	public boolean returnTransaction(Integer transactionId){
		final Transaction transaction = this.transactionRepository.findById(transactionId).orElseThrow(() ->
		new IllegalArgumentException("TRANSACTION-NOT-FOUND"));
				this.transactionRepository.delete(transaction);
		return !this.transactionRepository.findById(transactionId).isPresent();
	}


}
