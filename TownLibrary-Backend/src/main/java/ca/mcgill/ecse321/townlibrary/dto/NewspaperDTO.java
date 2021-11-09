package ca.mcgill.ecse321.townlibrary.dto;

import ca.mcgill.ecse321.townlibrary.model.Newspaper;

import java.util.Optional;

import ca.mcgill.ecse321.townlibrary.model.Status;
//import ca.mcgill.ecse321.townlibrary.model.Transaction;

public class NewspaperDTO {
	
	private int id;
	private String name;
	private Status status;
	
	private Integer libraryId;
	//private Transaction transaction;
	
	public NewspaperDTO() {
	}
	
	public NewspaperDTO(int id, String name, Integer libId) {
		this.id = id;
		this.name = name;
		// Newspapers cannot be reserved or checked out
		this.status = Status.AVAILABLE;	
		this.libraryId = libId;
		//this.transaction = trans;
	}
	
	public static NewspaperDTO fromModel(Newspaper n) {
		final NewspaperDTO dto = new NewspaperDTO();
		dto.id = n.getId();
		dto.name = n.getName();
		dto.status = n.getStatus();
		dto.libraryId = Optional.ofNullable(n.getLibrary())
				.map(x -> x.getId())
                .orElse(null);
		//dto.transaction = n.getTransaction();
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
