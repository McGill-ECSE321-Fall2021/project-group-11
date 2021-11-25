package ca.mcgill.ecse321.townlibrary.dto;

import java.util.Optional;

import ca.mcgill.ecse321.townlibrary.model.Book;
//import ca.mcgill.ecse321.townlibrary.model.Library;
//import ca.mcgill.ecse321.townlibrary.model.Transaction;
import ca.mcgill.ecse321.townlibrary.model.Status;

public class BookDTO {
	
	private int id;
	private String name;
	private Status status;
	private Integer libraryId;
	private	Integer transactionId;
	
	public BookDTO() {
	}
	
	public BookDTO(int id, String name, Status status, Integer libId) {
		this.id = id;
		this.name = name;
		this.status = status;
		this.libraryId = libId;
		//this.transaction = trans;
	}
	
	public static BookDTO fromModel(Book b) {
		final BookDTO dto = new BookDTO();
		dto.id = b.getId();
		dto.name = b.getName();
		dto.status = b.getStatus();
		dto.libraryId = Optional.ofNullable(b.getLibrary())
				.map(x -> x.getId())
                .orElse(null);
		dto.transactionId = Optional.ofNullable(b.getTransaction())
				.map(x -> x.getId())
				.orElse(null);
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
	
	public Integer getLibraryId() {
        return libraryId;
    }
	
//	public Transaction getTransaction() {
//        return transaction;
//    }

}
