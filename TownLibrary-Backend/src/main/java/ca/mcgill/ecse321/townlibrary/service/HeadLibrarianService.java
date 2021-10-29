package ca.mcgill.ecse321.townlibrary.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.repository.*;

@Service
public class HeadLibrarianService {

    @Autowired
    private HeadLibrarianRepository headLibrarianRepository;

    @Autowired
    private LibraryRepository libraryRepository;

    /**
     * Creates a head librarian
     *
     * @param lib       The library (non-null)
     * @param name      The head librarian's name (non-empty)
     * @param address   The head librarian's address (non-empty)
     *
     * @return          The head librarian instance
     *
     * @throws IllegalArgumentException problems with the arguments (comma separated)
     */
    @Transactional
    public HeadLibrarian createHeadLibrarian(Library lib, String name, String address) {
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

        if (errmsg.length() != 0) {
            // Delete the trailing ","
            errmsg.deleteCharAt(errmsg.length() - 1);
            throw new IllegalArgumentException(errmsg.toString());
        }

        final HeadLibrarian u = new HeadLibrarian();
        u.setName(name);
        u.setAddress(address);
        u.setLibrary(lib);
        this.headLibrarianRepository.save(u);

        // update the head librarian in the library-side as well
        lib.setHeadLibrarian(u);
        this.libraryRepository.save(lib);

        return u;
    }
}
