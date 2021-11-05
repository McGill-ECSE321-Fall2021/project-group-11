package ca.mcgill.ecse321.townlibrary.dto;

import ca.mcgill.ecse321.townlibrary.model.MusicAlbum;
import ca.mcgill.ecse321.townlibrary.model.Library;
import ca.mcgill.ecse321.townlibrary.model.Status;
import ca.mcgill.ecse321.townlibrary.model.Transaction;

public class MusicAlbumDTO {
	
	private int id;
	private String name;
	private Status status;
	
	private Library library;
	private Transaction transaction;
	
	public MusicAlbumDTO() {
	}
	
	public MusicAlbumDTO(int id, String name, Status status, Library lib, Transaction trans) {
		this.id = id;
		this.name = name;
		this.status = status;
		this.library = lib;
		this.transaction = trans;
	}
	
	public static MusicAlbumDTO fromModel(MusicAlbum m) {
		final MusicAlbumDTO dto = new MusicAlbumDTO();
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
