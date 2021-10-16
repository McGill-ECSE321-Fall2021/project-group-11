package ca.mcgill.ecse321.townlibrary.repository;

import ca.mcgill.ecse321.townlibrary.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, Integer> {

    /**
     * Searches UserRoles by a fragment of their name.
     *
     * @param name  The fragment of their name.
     *
     * @return      A potentially empty list of UserRoles.
     */
    List<UserRole> findByNameContaining(String name);
}