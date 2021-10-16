package ca.mcgill.ecse321.townlibrary.repository;

import ca.mcgill.ecse321.townlibrary.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.sql.Timestamp;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

    /**
     * Searches all the Transactions associated with the UserRole.
     *
     * @param userRole  The UserRole that the tasks are associated with.
     *
     * @return          A potentially empty list of Transactions.
     */
    List<Transaction> findByUserRole(UserRole userRole);

    /**
     * Searches all the Transactions associated with the UserRole that have an
     * EndDate after a certain date and time.
     *
     * @param userRole  The UserRole that the tasks are associated with.
     * @param timestamp The date and time.
     *
     * @return          A potentially empty list of Transactions.
     */
    List<Transaction> findByUserRoleAndEndDateAfter(UserRole userRole, Timestamp timestamp);
}