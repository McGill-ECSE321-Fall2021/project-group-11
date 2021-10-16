package ca.mcgill.ecse321.townlibrary.repository;

import ca.mcgill.ecse321.townlibrary.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OnlineMemberRepository extends CrudRepository<OnlineMember, Integer>{

    List<OnlineMember> findByUserRole(UserRole userRole);
    List<OnlineMember> findByAddress(String address);
   
    // not sure again :')
    OnlineMember findByEmail(String email);
    OnlineMember findByUsername(String username);
    OnlineMember findByUserId(int userId);

    
}
