package ca.mcgill.ecse321.townlibrary.repository;

import ca.mcgill.ecse321.townlibrary.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OnlineMemberRepository extends CrudRepository<OnlineMember, Integer>{

    List<OnlineMember> findByAddress(String address);
    OnlineMember findByEmail(String email);
    OnlineMember findByUsername(String username);
    
}
