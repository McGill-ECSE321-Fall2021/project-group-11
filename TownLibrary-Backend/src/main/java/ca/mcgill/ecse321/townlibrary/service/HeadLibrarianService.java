package ca.mcgill.ecse321.townlibrary.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.repository.*;

import java.util.*;

@Service
public class HeadLibrarianService {

    @Autowired
    private HeadLibrarianRepository headLibrarianRepository;

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private PasswordValidator passwordValidator;

    /**
     * Creates a head librarian
     *
     * @param lib       The library (non-null)
     * @param name      The head librarian's name (non-empty)
     * @param address   The head librarian's address (non-empty)
     * @param password  The member's password (see {@see PasswordValidator})
     *
     * @return          The head librarian instance
     *
     * @throws IllegalArgumentException problems with the arguments (comma separated)
     */
    @Transactional
    public HeadLibrarian createHeadLibrarian(Library lib, String name, String address, String password) {
        final StringBuilder errmsg = new StringBuilder();
        if (lib == null)
            errmsg.append("NULL-LIBRARY,");
        if (lib != null && lib.hasHeadLibrarian())
            errmsg.append("DUP-HEAD-LIBRARIAN,");
        if (name != null)
            name = name.trim();
        if (name == null || name.isEmpty())
            errmsg.append("EMPTY-NAME,");
        if (address != null)
            address = address.trim();
        if (address == null || address.isEmpty())
            errmsg.append("EMPTY-ADDRESS,");

        this.passwordValidator.validatePasswordCriteria(password, msg -> errmsg.append(msg).append(','));

        if (errmsg.length() != 0) {
            // Delete the trailing ","
            errmsg.deleteCharAt(errmsg.length() - 1);
            throw new IllegalArgumentException(errmsg.toString());
        }

        final HeadLibrarian u = new HeadLibrarian();
        u.setName(name);
        u.setAddress(address);
        u.setPassword(password);
        u.setLibrary(lib);
        this.headLibrarianRepository.save(u);

        // update the head librarian in the library-side as well
        lib.setHeadLibrarian(u);
        this.libraryRepository.save(lib);

        return u;
    }

    /**
     * Retrieves a head librarian by its id.
     *
     * @param id    The id
     *
     * @return      The head librarian or null if no such id exists
     */
    @Transactional
    public HeadLibrarian getHeadLibrarian(int id) {
        return this.headLibrarianRepository.findById(id).orElse(null);
    }

    /**
     * Authenticates a head librarian by its id and password.
     *
     * @param id        The id
     * @param password  The password
     *
     * @return          true if credentials are correct, false otherwise
     */
    @Transactional
    public boolean authenticateHeadLibrarian(int id, final String password) {
        return this.headLibrarianRepository.findById(id)
                .map(Librarian::getPassword)
                .map(p -> p.equals(password))
                .orElse(false);
    }

    /**
     * Retrieves all the head librarians registered under this system.
     *
     * @return all the head librarians
     */
    @Transactional
    public List<HeadLibrarian> getAllHeadLibrarians() {
        final ArrayList<HeadLibrarian> list = new ArrayList<>();
        for (final HeadLibrarian u : this.headLibrarianRepository.findAll())
            list.add(u);
        return list;
    }
}
