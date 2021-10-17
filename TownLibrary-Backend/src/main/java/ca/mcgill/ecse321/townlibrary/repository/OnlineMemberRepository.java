package ca.mcgill.ecse321.townlibrary.repository;

import ca.mcgill.ecse321.townlibrary.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OnlineMemberRepository extends CrudRepository<OnlineMember, Integer>{

    /** List all members with a certain address
     * 
     * @param address   -   String representing address
     * @return              List of Online Members that have that address
     */ 
    List<OnlineMember> findByAddress(String address);

    /** Return member with a specific email
     * 
     * @param email -   String representing an email address
     * @return          Online member with that email address
     */
    OnlineMember findByEmail(String email);

    /** Return member with a specific username
     * 
     * @param username  -   String representing username
     * @return              Online member with that username
     */
    OnlineMember findByUsername(String username);
    
}
