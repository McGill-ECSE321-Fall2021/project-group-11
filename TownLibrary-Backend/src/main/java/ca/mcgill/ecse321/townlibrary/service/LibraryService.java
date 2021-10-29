package ca.mcgill.ecse321.townlibrary.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.repository.*;

import java.util.*;

@Service
public class LibraryService {

    @Autowired
    private LibraryRepository libraryRepository;

    /**
     * Creates a library
     *
     * @param id        The id (unique)
     * @param address   The library's address (non-empty)
     *
     * @return          The library instance
     *
     * @throws IllegalArgumentException problems with the arguments (comma separated)
     */
    @Transactional
    public Library createLibrary(int id, String address) {
        final StringBuilder errmsg = new StringBuilder();
        if (this.libraryRepository.existsById(id))
            errmsg.append("DUP-LIBRARY,");
        if (address != null)
            address = address.trim();
        if (address == null || address.isEmpty())
            errmsg.append("EMPTY-ADDRESS,");

        if (errmsg.length() != 0) {
            // Delete the trailing ","
            errmsg.deleteCharAt(errmsg.length() - 1);
            throw new IllegalArgumentException(errmsg.toString());
        }

        final Library lib = new Library();
        lib.setId(id);
        lib.setAddress(address);
        this.libraryRepository.save(lib);
        return lib;
    }

    /**
     * Retrieves a library by it's id
     *
     * @param id    The id
     *
     * @return      The library or null if no such id exists
     */
    public Library getLibrary(int id) {
        return this.libraryRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves all the libraries registered under this system.
     *
     * @return all the libraries
     */
    @Transactional
    public List<Library> getAllLibraries() {
        final List<Library> lst = new ArrayList<>();
        for (final Library lib : this.libraryRepository.findAll())
            lst.add(lib);
        return lst;
    }
}