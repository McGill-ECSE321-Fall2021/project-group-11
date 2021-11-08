package ca.mcgill.ecse321.townlibrary.service;

import java.util.ArrayList;
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
	
	@Transactional
	public Transaction createTransaction(int id, Timestamp start, Timestamp end, UserRole user) {
		String error = "";
		if (id < 0) {
			error = error + "Unsupported Id.";
	    }
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
		error = error.trim();
	    if (error.length() > 0) {
	        throw new IllegalArgumentException(error);
	    }
	    
		Transaction transaction = new Transaction();
		transaction.setId(id);
		transaction.setStartDate(start);
		transaction.setEndDate(end);
		transaction.setUserRole(user);
		transactionRepository.save(transaction);
		return transaction;
	}
	
//	@Transactional
//    public Transaction getTransaction(int id) {
//		if (id < 0) {
//	        throw new IllegalArgumentException("Unsupported Id.");
//	    }
//        return this.transactionRepository.findById(id).orElse(null);
//    }
	
	@Transactional
	public List<Transaction> getTransactionsByUser(UserRole user) {
		if (user == null) {
	        throw new IllegalArgumentException("User cannot be empty.");
	    }
		return transactionRepository.findByUserRole(user);
	}
	
	@Transactional
    public List<Transaction> getAllTransactions() {
        final List<Transaction> transactions = new ArrayList<Transaction>();
        for (Transaction t : transactionRepository.findAll()) {
        	transactions.add(t);
        }
        return transactions;
    }
	
	

}
