package ca.mcgill.ecse321.townlibrary.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.repository.*;

import java.util.function.Consumer;

@Service
public class LibrarianService {

    @Autowired
    private LibrarianRepository librarianRepository;

    /**
     * Creates a librarian
     *
     * @param lib       The library (non-null)
     * @param name      The librarian's name (non-empty)
     * @param address   The librarian's address (non-empty)
     *
     * @return          The librarian instance
     *
     * @throws IllegalArgumentException problems with the arguments (comma separated)
     */
    @Transactional
    public Librarian createLibrarian(Library lib, String name, String address) {
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

        if (errmsg.length() != 0) {
            // Delete the trailing ","
            errmsg.deleteCharAt(errmsg.length() - 1);
            throw new IllegalArgumentException(errmsg.toString());
        }

        final Librarian u = new Librarian();
        u.setName(name);
        u.setAddress(address);
        u.setLibrary(lib);
        this.librarianRepository.save(u);
        return u;
    }
}