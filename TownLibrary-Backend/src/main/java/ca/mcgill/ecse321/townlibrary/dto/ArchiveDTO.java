package ca.mcgill.ecse321.townlibrary.dto;

import ca.mcgill.ecse321.townlibrary.model.Status;

public class ArchiveDTO {
	
	private int id;
	private String name;
	private Status status;
	
	public ArchiveDTO() {
	}
	
	public ArchiveDTO(int id, String name, Status status) {
		this.id = id;
		this.name = name;
		this.status = status;
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

}
