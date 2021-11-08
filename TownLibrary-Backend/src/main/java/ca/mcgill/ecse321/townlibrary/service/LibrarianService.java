package ca.mcgill.ecse321.townlibrary.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.repository.*;

import java.util.*;

@Service
public class LibrarianService {

    @Autowired
    private LibrarianRepository librarianRepository;

    @Autowired
    private PasswordValidator passwordValidator;

    /**
     * Creates a librarian
     *
     * @param lib       The library (non-null)
     * @param name      The librarian's name (non-empty)
     * @param address   The librarian's address (non-empty)
     * @param password  The member's password (see {@see PasswordValidator})
     *
     * @return          The librarian instance
     *
     * @throws IllegalArgumentException problems with the arguments (comma separated)
     */
    @Transactional
    public Librarian createLibrarian(Library lib, String name, String address, String password) {
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

        this.passwordValidator.validatePasswordCriteria(password, msg -> errmsg.append(msg).append(','));

        if (errmsg.length() != 0) {
            // Delete the trailing ","
            errmsg.deleteCharAt(errmsg.length() - 1);
            throw new IllegalArgumentException(errmsg.toString());
        }

        final Librarian u = new Librarian();
        u.setName(name);
        u.setAddress(address);
        u.setPassword(password);
        u.setLibrary(lib);
        this.librarianRepository.save(u);
        return u;
    }

    /**
     * Retrieves a librarian by its id.
     *
     * Note that this method may also query a head librarian.
     *
     * @param id    The id
     *
     * @return      The librarian or null if no such id exists
     */
    @Transactional
    public Librarian getLibrarian(int id) {
        return this.librarianRepository.findById(id).orElse(null);
    }

    /**
     * Authenticates a librarian by its id and password.
     *
     * Note that this method may also be used to on a head librarian.
     *
     * @param id        The id
     * @param password  The password
     *
     * @return          true if credentials are correct, false otherwise
     */
    @Transactional
    public boolean authenticateLibrarian(int id, String password) {
        return this.librarianRepository.findById(id)
                .map(Librarian::getPassword)
                .map(p -> p.equals(password))
                .orElse(false);
    }

    /**
     * Retrieves all the librarians registered under this system.
     *
     * @return all the librarians
     */
    @Transactional
    public List<Librarian> getAllLibrarians() {
        final ArrayList<Librarian> list = new ArrayList<>();
        for (final Librarian u : this.librarianRepository.findAll())
            list.add(u);
        return list;
    }
    /**
     * Deletes a librarian by their Id
     * @param id The librarian's id
     * @return true if the librarian was succesfully deleted, false otherwise
     * 
     * @throws IllegalArgumentException if no such librarian with the given id exists
     */
    @Transactional
    public boolean deleteLibrarian(int id){
        try {
            this.librarianRepository.delete(this.librarianRepository.findById(id).get());
        } catch (Exception e) {
            throw new IllegalArgumentException("LIBRARIAN-NOT-FOUND");
        }
        return !this.librarianRepository.findById(id).isPresent();
    }
}