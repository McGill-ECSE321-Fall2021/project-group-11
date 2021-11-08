package ca.mcgill.ecse321.townlibrary.dto;

import java.sql.Timestamp;
import java.util.Optional;

import ca.mcgill.ecse321.townlibrary.model.Transaction;
import ca.mcgill.ecse321.townlibrary.model.UserRole;

public class TransactionDTO {

	public int id;
    public Timestamp startDate;
    public Timestamp endDate;
    public Integer userId;

    public static TransactionDTO fromModel(Transaction t) {
    	final TransactionDTO dto = new TransactionDTO();
    	dto.id = t.getId();
    	dto.startDate = t.getStartDate();
    	dto.endDate = t.getEndDate();
    	dto.userId = Optional.ofNullable(t.getUserRole())
				.map(x -> x.getId())
				.orElse(null);
    	return dto;
    }
}
