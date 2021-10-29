package ca.mcgill.ecse321.townlibrary.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.repository.*;

import java.util.*;

@Service
public class OnlineMemberService {

    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private PasswordValidator passwordValidator;

    @Autowired
    private OnlineMemberRepository onlineMemberRepository;

    /**
     * Creates a online member
     *
     * @param lib       The library (non-null)
     * @param name      The member's name (non-empty)
     * @param address   The member's address (non-empty)
     * @param email     The member's email (unique, see {@see EmailValidator})
     * @param username  The member's username (non-empty, unique)
     * @param password  The member's password (see {@see PasswordValidator})
     *
     * @return          The member instance
     *
     * @throws IllegalArgumentException problems with the arguments (comma separated)
     */
    @Transactional
    public OnlineMember createOnlineMember(Library lib, String name, String address,
                                           String email, String username, String password) {
        final StringBuilder errmsg = new StringBuilder();
        if (lib == null)
            errmsg.append("NULL-LIBRARY,");
        if (name != null)
            name = name.trim();
        if (name == null || name.isEmpty())
            errmsg.append("EMPTY-NAME,");
        if (address != null)
            address = address.trim();
        if (address == null || address.isEmpty())
            errmsg.append("EMPTY-ADDRESS,");

        if (username != null)
            username = username.trim();
        if (username == null || username.isEmpty())
            errmsg.append("EMPTY-USERNAME,");
        else if (this.onlineMemberRepository.findByUsername(username) != null)
            errmsg.append("DUP-USERNAME,");

        if (this.emailValidator.validateEmailCriteria(email, msg -> errmsg.append(msg).append(',')))
            if (this.onlineMemberRepository.findByEmail(email) != null)
                errmsg.append("DUP-EMAIL,");
        this.passwordValidator.validatePasswordCriteria(password, msg -> errmsg.append(msg).append(','));

        if (errmsg.length() != 0) {
            // Delete the trailing ","
            errmsg.deleteCharAt(errmsg.length() - 1);
            throw new IllegalArgumentException(errmsg.toString());
        }

        final OnlineMember u = new OnlineMember();
        u.setName(name);
        u.setAddress(address);
        u.setLibrary(lib);
        u.setEmail(email);
        u.setUsername(username);
        u.setPassword(password);
        this.onlineMemberRepository.save(u);
        return u;
    }

    /**
     * Retrieves a online member by its id.
     *
     * @param id    The id
     *
     * @return      The online member or null if no such id exists
     */
    @Transactional
    public OnlineMember getOnlineMember(int id) {
        return this.onlineMemberRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves a online member by its username.
     *
     * @param username  The username
     *
     * @return          The online member or null if no such id exists
     */
    @Transactional
    public OnlineMember getOnlineMemberByUsername(String username) {
        return this.onlineMemberRepository.findByUsername(username);
    }

    /**
     * Retrieves all the online members registered under this system.
     *
     * @return all the online members
     */
    @Transactional
    public List<OnlineMember> getAllOnlineMembers() {
        final ArrayList<OnlineMember> list = new ArrayList<>();
        for (final OnlineMember u : this.onlineMemberRepository.findAll())
            list.add(u);
        return list;
    }
}
