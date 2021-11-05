package ca.mcgill.ecse321.townlibrary.dto;

import ca.mcgill.ecse321.townlibrary.model.Library;
import ca.mcgill.ecse321.townlibrary.model.Movie;
import ca.mcgill.ecse321.townlibrary.model.Status;
import ca.mcgill.ecse321.townlibrary.model.Transaction;

public class MovieDTO {
	
	private int id;
	private String name;
	private Status status;
	
	private Library library;
	private Transaction transaction;
	
	public MovieDTO() {
	}
	
	public MovieDTO(int id, String name, Status status, Library lib, Transaction trans) {
		this.id = id;
		this.name = name;
		this.status = status;
		this.library = lib;
		this.transaction = trans;
	}
	
	public static MovieDTO fromModel(Movie m) {
		final MovieDTO dto = new MovieDTO();
		dto.id = m.getId();
		dto.name = m.getName();
		dto.status = m.getStatus();
		dto.library = m.getLibrary();
		dto.transaction = m.getTransaction();
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
