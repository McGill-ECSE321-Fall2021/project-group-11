package ca.mcgill.ecse321.townlibrary.dto;

import java.util.Optional;

import ca.mcgill.ecse321.townlibrary.model.Transaction;
import ca.mcgill.ecse321.townlibrary.model.TransactionType;

public class TransactionDTO {

	public int id;
    public Long startDate;
    public Long endDate;
    public Integer userId;
	public TransactionType type;

    public static TransactionDTO fromModel(Transaction t) {
    	final TransactionDTO dto = new TransactionDTO();
    	dto.id = t.getId();
    	dto.startDate = t.getStartDate().getTime();
    	dto.endDate = t.getEndDate().getTime();
    	dto.userId = Optional.ofNullable(t.getUserRole())
				.map(x -> x.getId())
				.orElse(null);
		dto.type = t.getType();
    	return dto;
    }
}
