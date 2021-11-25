package ca.mcgill.ecse321.townlibrary.dto;
import java.util.Optional;

import ca.mcgill.ecse321.townlibrary.model.Item;
import ca.mcgill.ecse321.townlibrary.model.Status;

public class ItemDTO {
    public int id;
    public String name;
    public Integer libraryId;
    public Status status;
    public String itemType;
    public Integer transactionId;


    public static ItemDTO fromModel(Item item){
        ItemDTO dto = new ItemDTO();
        dto.itemType = item.getClass().getSimpleName();
        dto.id = item.getId();
		dto.name = item.getName();
		dto.status = item.getStatus();
		dto.libraryId = Optional.ofNullable(item.getLibrary())
				.map(x -> x.getId())
                .orElse(null);
		dto.transactionId = Optional.ofNullable(item.getTransaction())
				.map(x -> x.getId())
				.orElse(null); 
        return dto;
    }

}
