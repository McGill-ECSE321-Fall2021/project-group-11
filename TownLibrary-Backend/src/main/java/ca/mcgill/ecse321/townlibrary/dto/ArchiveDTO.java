package ca.mcgill.ecse321.townlibrary.dto;

import java.util.Optional;

import ca.mcgill.ecse321.townlibrary.model.Archive;
//import ca.mcgill.ecse321.townlibrary.model.Library;
//import ca.mcgill.ecse321.townlibrary.model.Transaction;
import ca.mcgill.ecse321.townlibrary.model.Status;

public class ArchiveDTO {
	
	private int id;
	private String name;
	private Status status;
	
	private Integer libraryId;
	//private Transaction transaction;
	
	public ArchiveDTO() {
	}
	
	public ArchiveDTO(int id, String name, Integer libId) {
		this.id = id;
		this.name = name;
		// Archives cannot be reserved or checked out
		this.status = Status.AVAILABLE;	
		this.libraryId = libId;
		//this.transaction = trans;
	}
	
	public static ArchiveDTO fromModel(Archive a) {
		final ArchiveDTO dto = new ArchiveDTO();
		dto.id = a.getId();
		dto.name = a.getName();
		dto.status = a.getStatus();
		dto.libraryId = Optional.ofNullable(a.getLibrary())
				.map(x -> x.getId())
                .orElse(null);
		//dto.transaction = a.getTransaction();
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
