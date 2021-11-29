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

	/**
	 * Creates a new transaction from the specified arguments
	 * @param start Start Date
	 * @param end	End Date
	 * @param user	User
	 * @param type	Transaction Type
	 * @return The newly created Transaction
	 * 
	 * @throws IllegalArgumentException Parameter Checks fail
	 */
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

	/**
	 * Returns the transaction with the given id
	 * @param id
	 * @return The transaction with the specified id
	 * 
	 * @throws IllegalArgumentException Id is not valid or the transaction does not exist
	 */
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
	/**
	 *  Renews the given transaction for an additional 2 weeks.
	 * @param transaction
	 * @return The updated transaction
	 * 
	 * @throws IllegalArgumentException Request is made outside of the alloted timeframe
	 */

	@Transactional
	public Transaction renewTransaction(Transaction transaction){

		if (System.currentTimeMillis() <= transaction.getEndDate().getTime() - 1000 * 86400 * 7) {
			throw new IllegalArgumentException("OUT-OF-TIMEFRAME");
		}

		Timestamp newEndDate = new Timestamp(transaction.getEndDate().getTime() + 1000 * 86400 * 14);
		transaction.setEndDate(newEndDate);
		transactionRepository.save(transaction);

		return transaction;
	}
	/**
	 * Deletes a transaction with the specified id
	 * @param transactionId
	 * @return True if the transaction was deleted
	 * 
	 * @throws IllegalArgumentException	Transaction does not exist or the request is made outside of 
	 * the alotted timeframe.
	 */
	@Transactional
	public boolean returnTransaction(Integer transactionId){
		final Transaction transaction = this.transactionRepository.findById(transactionId).orElseThrow(() ->
		new IllegalArgumentException("TRANSACTION-NOT-FOUND"));


		if (System.currentTimeMillis() > transaction.getEndDate().getTime() ){
			throw new IllegalArgumentException("OUT-OF-TIMEFRAME");
		}
				final Item item = this.itemRepository.findItemByTransaction(transaction);
				item.setTransaction(null);
				item.setStatus(Status.AVAILABLE);
				this.itemRepository.save(item);
				this.transactionRepository.delete(transaction);
		return !this.transactionRepository.findById(transactionId).isPresent();
	}


}
