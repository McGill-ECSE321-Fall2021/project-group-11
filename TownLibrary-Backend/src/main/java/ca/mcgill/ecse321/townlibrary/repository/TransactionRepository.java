package ca.mcgill.ecse321.townlibrary.repository;

import ca.mcgill.ecse321.townlibrary.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.sql.Timestamp;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

    List<Transaction> findByUserRole(UserRole userRole);

    List<Transaction> findByUserRoleAndEndDateAfter(UserRole userRole, Timestamp Timestamp);
}