package ca.mcgill.ecse321.townlibrary.model;

import javax.persistence.*;

@Entity
public abstract class Item {
	
	private int id;
    private Status status;
    private String name;
    
    private Transaction transaction;

    private Library library;

    @Id
    public int getId() {
    	return this.id;
    }
  
    public void setId(int value) {
    	this.id = value;
    }

    public Status getStatus() {
    	return this.status;
    }

    public void setStatus(Status value) {
    	this.status = value;
    }

    public String getName() {
    	return this.name;
    }

    public void setName(String value) {
    	this.name = value;
    }

    @OneToOne
    public Transaction getTransaction() {
    	return this.transaction;
    }

    public void setTransaction(Transaction transaction) {
    	this.transaction = transaction;
    }

    @ManyToOne
    public Library getLibrary() {
    	return this.library;
    }

    public void setLibrary(Library library) {
    	this.library = library;
    }
}