package ca.mcgill.ecse321.townlibrary.dto;

import ca.mcgill.ecse321.townlibrary.model.Book;
import ca.mcgill.ecse321.townlibrary.model.Library;
import ca.mcgill.ecse321.townlibrary.model.Transaction;
import ca.mcgill.ecse321.townlibrary.model.Status;

public class BookDTO {
	
	private int id;
	private String name;
	private Status status;
	
	private Library library;
	private Transaction transaction;
	
	public BookDTO() {
	}
	
	public BookDTO(int id, String name, Status status, Library lib, Transaction trans) {
		this.id = id;
		this.name = name;
		this.status = status;
		this.library = lib;
		this.transaction = trans;
	}
	
	public static BookDTO fromModel(Book b) {
		final BookDTO dto = new BookDTO();
		dto.id = b.getId();
		dto.name = b.getName();
		dto.status = b.getStatus();
		dto.library = b.getLibrary();
		dto.transaction = b.getTransaction();
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
