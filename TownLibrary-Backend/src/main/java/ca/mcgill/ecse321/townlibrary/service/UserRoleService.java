package ca.mcgill.ecse321.townlibrary.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.repository.*;

import java.util.*;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    /**
     * Retrieves a user role by its id.
     *
     * @param id    The id
     *
     * @return      The user role or null if no such id exists
     */
    @Transactional
    public UserRole getUserRole(int id) {
        return this.userRoleRepository.findById(id).orElse(null);
    }
}