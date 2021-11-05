package ca.mcgill.ecse321.townlibrary.dto;

import ca.mcgill.ecse321.townlibrary.model.Archive;
import ca.mcgill.ecse321.townlibrary.model.Library;
import ca.mcgill.ecse321.townlibrary.model.Transaction;
import ca.mcgill.ecse321.townlibrary.model.Status;

public class ArchiveDTO {
	
	private int id;
	private String name;
	private Status status;
	
	private Library library;
	private Transaction transaction;
	
	public ArchiveDTO() {
	}
	
	public ArchiveDTO(int id, String name, Library lib, Transaction trans) {
		this.id = id;
		this.name = name;
		// Archives cannot be reserved or checked out
		this.status = Status.AVAILABLE;	
		this.library = lib;
		this.transaction = trans;
	}
	
	public static ArchiveDTO fromModel(Archive a) {
		final ArchiveDTO dto = new ArchiveDTO();
		dto.id = a.getId();
		dto.name = a.getName();
		dto.status = a.getStatus();
		dto.library = a.getLibrary();
		dto.transaction = a.getTransaction();
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
