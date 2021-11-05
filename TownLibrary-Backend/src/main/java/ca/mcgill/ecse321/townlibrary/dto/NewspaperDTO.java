package ca.mcgill.ecse321.townlibrary.dto;

import ca.mcgill.ecse321.townlibrary.model.Newspaper;
import ca.mcgill.ecse321.townlibrary.model.Library;
import ca.mcgill.ecse321.townlibrary.model.Status;
import ca.mcgill.ecse321.townlibrary.model.Transaction;

public class NewspaperDTO {
	
	private int id;
	private String name;
	private Status status;
	
	private Library library;
	private Transaction transaction;
	
	public NewspaperDTO() {
	}
	
	public NewspaperDTO(int id, String name, Library lib, Transaction trans) {
		this.id = id;
		this.name = name;
		// Newspapers cannot be reserved or checked out
		this.status = Status.AVAILABLE;	
		this.library = lib;
		this.transaction = trans;
	}
	
	public static NewspaperDTO fromModel(Newspaper n) {
		final NewspaperDTO dto = new NewspaperDTO();
		dto.id = n.getId();
		dto.name = n.getName();
		dto.status = n.getStatus();
		dto.library = n.getLibrary();
		dto.transaction = n.getTransaction();
		return dto;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public Library getLibrary() {
        return library;
    }
	
	public Transaction getTransaction() {
        return transaction;
    }

}
