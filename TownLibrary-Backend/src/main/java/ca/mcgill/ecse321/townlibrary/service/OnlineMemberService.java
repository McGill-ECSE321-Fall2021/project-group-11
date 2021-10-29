package ca.mcgill.ecse321.townlibrary.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.repository.*;

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
     * @param email     The member's email (non-empty, unique)
     * @param username  The member's username (non-empty, unique)
     * @param password  The member's password (see {@link this#validatePasswordCriteria(String)})
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

        // XXX: Uniqueness of username?
        // XXX: Uniqueness of email?

        this.emailValidator.validateEmailCriteria(email, msg -> errmsg.append(msg).append(','));
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
}
