package ca.mcgill.ecse321.townlibrary.dto;

import java.sql.Timestamp;

import ca.mcgill.ecse321.townlibrary.model.Transaction;
import ca.mcgill.ecse321.townlibrary.model.UserRole;

public class TransactionDTO {
	
	private int id;
    private Timestamp startDate;
    private Timestamp endDate;
    private UserRole userRole;
    
    public TransactionDTO() {
    }
    
    public TransactionDTO(int id, Timestamp start, Timestamp end, 
    					  UserRole user) {
    	this.id = id;
    	this.startDate = start;
    	this.endDate = end;
    	this.userRole = user;
    }
    
    public static TransactionDTO fromModel(Transaction t) {
    	final TransactionDTO dto = new TransactionDTO();
    	dto.id = t.getId();
    	dto.startDate = t.getStartDate();
    	dto.endDate = t.getEndDate();
    	dto.userRole = t.getUserRole();
    	return dto;
    }
    
    public int getId() {
		return id;
	}
    
    public Timestamp getStartDate() {
		return startDate;
	}
    
    public Timestamp getEndDate() {
		return endDate;
	}
    
    public UserRole getUser() {
		return userRole;
	}
}
