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
		if (user == null)
			throw new IllegalArgumentException("EMPTY-USER");

		Transaction transaction = new Transaction();
		transaction.setId(id);
		transaction.setStartDate(start);
		transaction.setEndDate(end);
		transaction.setUserRole(user);
		transactionRepository.save(transaction);
		return transaction;
	}

	@Transactional
    public Transaction getTransaction(int id) {
        return this.transactionRepository.findById(id).orElse(null);
    }

	@Transactional
	public List<Transaction> getTransactionsByUser(UserRole user) {
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
