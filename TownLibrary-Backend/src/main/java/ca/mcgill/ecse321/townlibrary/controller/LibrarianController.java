package ca.mcgill.ecse321.townlibrary.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.townlibrary.dto.*;
import ca.mcgill.ecse321.townlibrary.model.*;
import ca.mcgill.ecse321.townlibrary.service.*;

@CrossOrigin(origins="*")
@RestController
public class LibrarianController {

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private LibrarianService librarianService;

    @GetMapping(value={ "/librarians", "/librarians/" })
    public ResponseEntity<?> getAllLibrarians() {
        final List<LibrarianDTO> us = this.librarianService.getAllLibrarians()
                .stream()
                .map(LibrarianDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(us);
    }

    @GetMapping(value={ "/librarians/{id}", "/librarians/{id}/" })
    public ResponseEntity<?> getLibrarian(@PathVariable("id") int id)  {
        final Librarian u = this.librarianService.getLibrarian(id);
        if (u == null)
            return ResponseEntity.badRequest().body("NOT-FOUND-LIBRARIAN");
        return ResponseEntity.ok(LibrarianDTO.fromModel(u));
    }

    // XXX: Which one between
    // *  /librarians/{name}?library={id}
    // *  /libraries/{id}/librarians/{name}
    @PostMapping(value={ "/librarians/{name}", "/librarians/{name}/" })
    public ResponseEntity<?> createLibrarian(
            @PathVariable("name") String name,
            @RequestParam String password,
            @RequestParam String address,
            @RequestParam int library) {

        try {
            final Library lib = this.libraryService.getLibrary(library);
            final Librarian u = this.librarianService.createLibrarian(lib, name, address, password);
            return ResponseEntity.ok().body(LibrarianDTO.fromModel(u));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}