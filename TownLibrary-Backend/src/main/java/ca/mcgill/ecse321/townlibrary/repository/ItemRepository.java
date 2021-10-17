package ca.mcgill.ecse321.townlibrary.repository;

import ca.mcgill.ecse321.townlibrary.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends CrudRepository<Item, Integer> {
	
	/**
	 * Searches Item by id.
	 * @param id				The item id.
	 * @return					id as an integer.
	 */
	Item findItemById(Integer id);
	
	/**
	 * Searches Item by the associated Transaction.
	 * @param transaction		The associated Transaction object.
	 * @return					Book associated with the searched transaction.
	 */
	Item findItemByTransaction(Transaction transaction);
	
}
