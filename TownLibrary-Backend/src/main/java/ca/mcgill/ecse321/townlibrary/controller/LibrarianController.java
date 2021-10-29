package ca.mcgill.ecse321.townlibrary.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.service.*;

@CrossOrigin(origins="*")
@RestController
public class LibrarianController {

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private LibrarianService librarianService;

    // TODO: Use DTO's... please...

    @GetMapping(value={ "/librarians", "/librarians/" })
    public ResponseEntity<?> getAllLibrarians() {
        final List<Librarian> us = this.librarianService.getAllLibrarians();
        return ResponseEntity.ok().body(us);
    }

    // XXX: Which one between
    // *  /librarians/{name}?library={id}
    // *  /libraries/{id}/librarians/{name}
    @PostMapping(value={ "/librarians/{name}", "/librarians/{name}/" })
    public ResponseEntity<?> createLibrarian(
            @PathVariable("name") String name,
            @RequestParam String address,
            @RequestParam int library) {

        try {
            final Library lib = this.libraryService.getLibrary(library);
            final Librarian u = this.librarianService.createLibrarian(lib, name, address);
            return ResponseEntity.ok().body(u);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}