package ca.mcgill.ecse321.townlibrary.dto;

import java.util.Optional;

//import ca.mcgill.ecse321.townlibrary.model.Library;
import ca.mcgill.ecse321.townlibrary.model.Status;
import ca.mcgill.ecse321.townlibrary.model.MusicAlbum;
//import ca.mcgill.ecse321.townlibrary.model.Transaction;

public class MusicAlbumDTO {
	
	private int id;
	private String name;
	private Status status;
	
	private Integer libraryId;
	private	Integer transactionId;
	
	public MusicAlbumDTO() {
	}
	
	public MusicAlbumDTO(int id, String name, Status status, Integer libId) {
		this.id = id;
		this.name = name;
		this.status = status;
		this.libraryId = libId;
		//this.transaction = trans;
	}
	
	public static MusicAlbumDTO fromModel(MusicAlbum m) {
		final MusicAlbumDTO dto = new MusicAlbumDTO();
		dto.id = m.getId();
		dto.name = m.getName();
		dto.status = m.getStatus();
		dto.libraryId = Optional.ofNullable(m.getLibrary())
				.map(x -> x.getId())
                .orElse(null);
		dto.transactionId = Optional.ofNullable(m.getTransaction())
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
